# 좋아요 설계

# 상세 설계
* 좋아요는 회원 혹은 게시글 만으로는 상태를 판별하기 어렵기 때문에 복합키로 관리합니다.
* 복합키는 회원UUID, 게시글UUID 순으로 지정됩니다.
* uuid로 한 까닭은 구독과 마찬가지로 프로젝트 컨벤션으로 fk는 내부 식별자(id)를 사용하기로 하였지만,
* 해당 도메인은 가볍고, 효율적인 조회/관리 등을 위해서 외부 식별자를 fk로 사용하였습니다.
* 두개의 fk(외부 식별자)로 pk를 구성하였습니다.
* 복합키 덕분에 중복된 좋아요는 불가능합니다.
* 회원에 속한 좋아요, 게시글에 속한 좋아요 총 두가지 형태로 api를 제공합니다.

## API 설계
```
[GET] /likes/belong/member/{memberUUID} : 회원에 속한 좋아요
[GET] /likes/belong/post/{postUUID} : 게시글에 속한 좋아요
[POST] /likes/like
[DELETE] /likes/dislike
```

## Json Body 예시
```json
[CreateLikes]
{
  "memberUUID": "61da7e2f-709e-4dc3-a13c-47dd026c21d1",
  "postUUID": "6af8c75d-9883-4687-bba1-8e6fe594e24f"
}

[DeleteLikes]
{
  "memberUUID": "87d5da2d-88ae-4b62-a5f0-3976c8947649",
  "postUUID": "8df6fcd7-65b2-4d3f-9a8d-529ff725219d"
}
```

## DB 설계
```sql
create table likes (
     timestamp integer,
     member_uuid binary(16) not null,
     post_uuid binary(16) not null,
     primary key (member_uuid, post_uuid)
);
```