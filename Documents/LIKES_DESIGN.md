# 좋아요 설계

## 명칭
* like는 mysql의 예약어입니다.
* 따라서 이를 사용하게 되면 에러가 발생합니다. 따라서 좋아요의 명칭은 모두 likes로 통일합니다.
* 이는 좋아요들 을 의미하는 '복수'가 아니고 '단수 명칭'으로 사용됩니다.

# 상세 설계
* 좋아요는 회원 혹은 게시글 만으로는 상태를 판별하기 어렵기 때문에 복합키로 관리합니다.
* 복합키는 회원UUID, 게시글id 순으로 지정됩니다.
* 두개의 fk로 pk를 구성하였습니다.
* 복합키 덕분에 중복된 좋아요는 불가능합니다.
* 회원에 속한 좋아요, 게시글에 속한 좋아요 총 두가지 형태로 api를 제공합니다.
* 회원은 회원이 어떤 게시글에 좋아요를 눌렀는지 파악이 가능하고, 게시글을 어떤 회원들이 좋아요를 눌렀는지 파악이 가능합니다.
* 또한 게시글의 총 좋아요 수를 리턴하는 api가 존재하여, 클라이언트에서 해당 api를 호출하여 게시글에 적절한 좋아요 수를 리턴하면됩니다.
* 좋아요는 컬렉션 조회시 80개씩 페이징한다.
* [복합키를 사용하는 테이블에서 timestamp 기반 no-offset 페이징](https://github.com/liveforone/howru/blob/master/Documents/NO_OFFSET_IN_COMPOSITE_KEY_TABLE.md)을 참조하면 복합키를 사용하는 테이블에서의 no-offset 동작방식을 알 수 있을것이다.

## API 설계
```
[GET] /likes/count/{postId} : 게시글의 좋아요 수
[GET] /likes/belong/member/{memberUUID} : 회원에 속한 좋아요
[GET] /likes/belong/post/{postId} : 게시글에 속한 좋아요
[POST] /likes/like
[DELETE] /likes/dislike
```

## Json Body 예시
```json
[CreateLikes]
{
  "memberUUID": "61da7e2f-709e-4dc3-a13c-47dd026c21d1",
  "postId": 2
}

[DeleteLikes]
{
  "memberUUID": "87d5da2d-88ae-4b62-a5f0-3976c8947649",
  "postId": 1
}
```

## DB 설계
```sql
create table likes (
     timestamp integer,
     member_uuid binary(16) not null,
     post_id binary(16) not null,
     primary key (member_uuid, post_id)
);
CREATE INDEX likes_post_id_idx ON Likes (post_id);
CREATE INDEX likes_timestamp_idx ON Likes (timestamp);
```

## 인덱스 설계 주의 사항
* 좋아요의 경우 member_uuid를 where에 사용할 경우 인덱스가 동작한다.
* 그러나 post_id만 where 절에 사용할 경우 인덱스가 동작하지 않는다. 복합키의 pk 인덱스는 순서에 따라 member_uuid, post_id 로 구성되어 있기 때문이다.
* 따라서 post_id로 조회하는 상황이 있기에 post_id에 대한 인덱스를 만들어 주었다. 복합키를 사용할때 인덱스는 순서에 민감하므로 추가적인 인덱스 구성이 필요한지 살펴보아야한다.