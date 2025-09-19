# 구독(팔로잉) 설계

## 주의
* mysql & postgresql 예약어에 following이 들어갑니다.
* 이에 따라 subscribe로 단어를 대체하여 사용합니다.
* 종종 following, follow 등 단어가 혼용되어 사용될 수 있으나, 테이블 이름은 Subscribe로 사용되는 점을 주의합니다.

## 용어
* follower는 팔로우하는 사람을 의미하며, followee는 팔로우를 당하는 사람을 의미합니다.
* 즉 a가 b를 팔로우한다면, a는 b의 follower이며, b는 a의 followee입니다.

## 상세 설계
* 팔로우는 팔로워, 혹은 팔로잉을 당하는 주체인 followee 만으로 팔로우의 관계를 나타낼 수 없다.
* 둘 모두의 상태가 팔로우를 결정하기 때문에 복합키를 쓰는 것이 옳다.
* 복합키는 jpa에서 두가지 방법을 지원하나, 해당 엔티티는 `IdClass`를 사용하였다.
* 프로젝트 컨벤션에서 외래키는 pk를 쓰도록 기술하였지만, 외부에 pk를 빈번하게 노출하는 구독 엔티티의 특성상
* 회원의 `uuid`를 pk로 사용하도록하였다.
* 팔로잉, 팔로워에 대한 수를 리턴하는 api를 제공한다.
* 서로 팔로우하는 관계인 맞팔로우에 대한 메서드를 제공하며, 이는 잠금 회원의 프로필에 접근하는 경우 사용한다.
* 언팔로우는 삭제처리한다.
* 게시글 도메인에 팔로잉하는 사람들의 리스트를 제공한다.
* 구독은 컬렉션 조회시 50개씩 페이징한다.
* `uuid`는 정렬이 되지 않으므로 `timestamp(db의 timestamp가 아니라 unix의 시간 시스템)`을 두어서 이를 기준으로 정렬하였다.
* 이를 이용해 아래에 기술된 `no-offset` 페이징에 활용하였다.
* [복합키를 사용하는 테이블에서 timestamp 기반 no-offset 페이징](https://github.com/liveforone/howru/blob/master/Documents/NO_OFFSET_IN_COMPOSITE_KEY_TABLE.md)을 참조하면 복합키를 사용하는 테이블에서의 `no-offset` 동작방식을 알 수 있을것이다.

## 설계시 중요하게 생각한 점
* 복잡성을 최대한 낮게 관리하려했으나, 팔로우의 데이터 양은 게시글 수준으로 많을 것으로 예상했다.
* 따라서 디그리(차수)를 최대한 적게 갖도록 조정하였다. 즉 최대한 가볍게 유지하였다.

## API 설계
```
[GET] /subscribes/{memberId}/following : 팔로잉 가져오기
[GET] /subscribes/{memberId}/follower : 팔로워 가져오기
[GET] /subscribes/{memberId}/count/following : 팔로잉 수 가져오기
[GET] /subscribes/{memberId}/count/follower : 팔로워 수 가져오기
[POST] /subscribes : 구독
[DELETE] /subscribes/unsubscribe : 구독 취소
```

## Json Body 설계
```json
[CreateSubscribe]
{
  "followeeUUID": "c2390ef7-2cae-4417-9ae5-94f44c334750",
  "followerUUID": "1cac45bd-2d55-4205-8b45-c65211ddf58d"
}

[UnsubscribeRequest]
{
  "followeeId": "c2390ef7-2cae-4417-9ae5-94f44c334750",
  "followerId": "1cac45bd-2d55-4205-8b45-c65211ddf58d"
}
```

## DB 설계
```sql
create table subscribe (
    timestamp integer,
    followee_id UUID not null,
    follower_id UUID not null,
    primary key (followee_id, follower_id)
);
CREATE INDEX subscribe_timestamp_idx ON Subscribe (timestamp);
```
