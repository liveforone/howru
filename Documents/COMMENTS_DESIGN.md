# 댓글 설계

## 명칭 문제
* sql에서 comment는 예약어이다.
* 따라서 이를 사용하게 되면 에러가 발생한다. 즉 ddl 자체가 불가능하다.
* 따라서 댓글에 대한 명칭은 모두 Comments로 통일한다.
* 이는 복수를 뜻하는 것이 아닌 댓글을 해당 프로젝트에서 나타내는 '명사'일 뿐이다.
* 해당 프로젝트는 mysql을 DB로 사용했기에 comments라는 네이밍이 문제가 없었지만, postgressql을 사용한다면 comments마저도 예약어로 분류되니 주의하라.

## 상세 설계
* 댓글은 수정/삭제가 가능합니다.
* 대댓글도 존재하며, 현재 서술하고 있는 댓글과는 다른 도메인입니다.
* 댓글은 상태가 존재하며, 변경이 발생하면 상태가 수정됨으로 변경됩니다.
* 따라서 댓글이 수정된 댓글인지 다른 사람들이 알 수 있습니다.
* 댓글의 경우 페이징시 15개씩 리밋걸어서 페이징 합니다.
* 댓글은 의견으로도 나타낼 수 있으며, 이러한 맥락에 따라 맞팔로우하고 있는 회원의 의견(댓글)을 확인하는 것이 가능합니다.
* 평소 어떤 의견을 맞팔로우하고 있는 회원이 가지고 있는지 확인 가능하도록 만들었습니다.
* 당연하게도 다른 사람의 댓글을 모두 확인하려면 맞팔로우한 상태여야만 합니다.
* 댓글은 100자의 길이 제한을 갖습니다. 이는 정책입니다.

## API 설계
```
[GET] /comments/{id}
[GET] /posts/{postId}/comments?lastId={lastId} : 댓글 페이지
[GET] /comments/my?lastId={lastId} : 내가가 작성한 댓글 목록
[GET] /members/{memberId}/comments?lastId={lastId} : 다른 사용자가 작성한 댓글 목록
[POST] /comments
[PATCH] /comments/{id}
[DELETE] /comments/{id}
```

## Json Body 예시
```json
[CreateComments]
{
  "writerId": "86bd534f-3044-47df-bf8f-7531b343870e",
  "postId": 1,
  "content": "comment"
}

[UpdateComments]
{
  "writerId": "86bd534f-3044-47df-bf8f-7531b343870e",
  "content": "updated comment"
}

[RemoveComments]
{
  "writerId": "86bd534f-3044-47df-bf8f-7531b343870e"
}
```

## DB 설계
```sql
create table comments (
     id bigint not null auto_increment,
     post_id bigint,
     writer_id bigint,
     content VARCHAR(100) not null,
     comments_state varchar(8) not null,
     created_date BIGINT(12) not null,
     primary key (id)
     foreign key (writer_id) references Member (id) on delete cascade
     foreign key (post_id) references Post (id) on delete cascade
);
```
