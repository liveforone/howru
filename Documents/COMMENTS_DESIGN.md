# 댓글 설계

## 상세 설계
* 댓글은 수정 삭제가 가능합니다.
* 대댓글도 존재하며, 현재 서술하고 있는 댓글과는 다른 도메인입니다.
* 댓글은 상태가 존재하며, 변경이 발생하면 상태가 수정됨으로 변경됩니다. 
* 따라서 댓글이 수정된 댓글인지 다른사람들이 알 수 있습니다.
* 댓글의 경우 페이징시 15개씩 리밋걸어서 페이징 합니다.
* 댓글은 의견으로도 나타낼 수 있으며, 이러한 맥락에 따라 맞팔로우하고 있는 회원의 의견(댓글)을 확인하는 것이 가능합니다.
* 평소 어떤 의견을 맞팔로우하고 있는 회원이 가지고 있는지 확인 가능하도록 만들었습니다.
* 당연하게도 다른 사람의 댓글을 모두 확인하려면 맞팔로우한 상태여야만 합니다.

## API 설계
```
[GET] /comments/detail/{uuid}
[GET] /comments/writer/{writerUUID} : 사용자가 작성한 댓글
[GET] /comments/post/{postUUID} : 게시글에 속한 댓글
[GET] /comments/someone/{writerUUID} : 다른 사용자가 작성한 게시글
[POST] /comments/create
[PUT] /comments/edit
[DELETE] /comments/delete
```

## Json Body 예시
```json
[CreateComments]
{
  "writerUUID": "86bd534f-3044-47df-bf8f-7531b343870e",
  "postUUID": "977920f9-b20f-414d-b2c9-48260025771c",
  "content": "comment"
}

[UpdateCommentsContent]
{
  "uuid": "6181dbf7-99de-4606-a828-0513dd7769a8",
  "writerUUID": "86bd534f-3044-47df-bf8f-7531b343870e",
  "content": "updated comment"
}

[DeleteComments]
{
  "uuid": "6181dbf7-99de-4606-a828-0513dd7769a8",
  "writerUUID": "86bd534f-3044-47df-bf8f-7531b343870e"
}
```

## DB 설계
```sql
create table comments (
     id bigint not null auto_increment,
     uuid BINARY(16) not null,
     post_id bigint,
     writer_id bigint,
     created_date BIGINT(12) not null,
     comments_state varchar(255) not null,
     content VARCHAR(100) not null,
     primary key (id)
     foreign key (writer_id) references Member (id) on delete cascade
     foreign key (post_id) references Post (id) on delete cascade
);
CREATE INDEX uuid_idx ON comments (uuid);
```