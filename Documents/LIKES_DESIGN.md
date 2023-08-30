# 좋아요 설계

# 상세 설계
* 좋아요는 회원 혹은 게시글 만으로는 상태를 판별하기 어렵기 때문에 복합키로 관리합니다.
* 복합키는 회원UUID, 게시글id 순으로 지정됩니다.
* 두개의 fk로 pk를 구성하였습니다.
* 복합키 덕분에 중복된 좋아요는 불가능합니다.
* 회원에 속한 좋아요, 게시글에 속한 좋아요 총 두가지 형태로 api를 제공합니다.
* 좋아요는 컬렉션 조회시 80개씩 페이징한다.
* [복합키를 사용하는 테이블에서 timestamp 기반 no-offset 페이징](https://github.com/liveforone/howru/blob/master/Documents/NO_OFFSET_IN_COMPOSITE_KEY_TABLE.md)을 참조하면 복합키를 사용하는 테이블에서의 no-offset 동작방식을 알 수 있을것이다.

## API 설계
```
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
CREATE INDEX likes_timestamp_idx ON Likes (timestamp);
```