# JPA에서 복합키로 조회 쿼리 최적화

## 복합키 사용 이유
* 해당 프로젝트에서는 팔로우 테이블과 좋아요 테이블에서 복합키를 사용했다.
* 하나의 값이 테이블의 상태를 표현하지 못한다면 당당하게 복합키를 사용하면된다.
* 필자의 경우 팔로워, 혹은 팔로잉을 당하는 주체(followee) 만으로는 팔로우 테이블의 상태를 표현하지 못하겠다고 생각하여
* 복합키로 팔로워(follower)와 팔로잉을 당하는 주체(followee)를 사용하였다.

## JPA에서 복합키 사용
* JPA에서는 두가지 복합키 사용법을 제공한다.
* 하나는 `EmbeddedId`이고 둘째는 `IdClass`이다.
* 필자는 `IdClass`를 사용하였다. 이 방법이 보다 편리하고 관리하기 쉬워보여서 사용했다.
* 특히 필자의 경우 조회시에 복합키 자체로 조회 하는 것 뿐만아니라 하나의 컬럼만으로도 조회하는 쿼리가 많아서 이렇게 결정하였다.

## 코틀린 복합키 사용 유의점
* 코틀린에서는 복합키를 사용할때 딱 한가지 유의점이 있는것 같다.
* 복합키 클래스에서 기본 생성자가 필요한데, 이렇게 기본생성자를 정의하지 말고,
* 아래처럼 값을 미리 할당하는 것이다. 이렇게 미리 할당해야 에러가 발생하지 않고 정상적으로 처리되며,
* 기본생성자 사용방식보다 훨씬 간편하다.
```kotlin
data class SubscribePk(
    val followeeUUID: UUID? = null,
    val followerUUID: UUID? = null
) : Serializable
```

## 복합키의 주의점1 : 순서
* 복합키 자체의 주의점도 있다.
* 바로 키의 순서이다. 특히나 인덱스가 정상적으로 작동하도록 순서를 주의해서 설정해야한다.
* 순서는 DB에 들어가서 확인하면 나오고, 필자의 경우 followeeUUID -> followerUUID 순으로 되어있었다.
* 따라서 반드시 조회시에 이 순서대로 집어넣어야 기본키 인덱스가 걸린다.

## 복합키의 주의점2 : 정렬 어려움
* 복합키의 두번째 주의점은 정렬이 어렵다는 것이다.
* 따라서 정렬가능한 컬럼을 삽입하면 아주 큰 도움이된다.
* 어떤것이 가볍고, 정확하게 정렬가능하며, 값을 쉽게 삽입할 수 있을까 고민하던중 금융권의 사례를 알게되었다.
* 금융권의 경우 날짜를 숫자로(`datetime`이 아닌) 집어넣는다는것이다. 
* `datetime` 타입의 경우 `int` 형보다 성능이 현저히 떨어지므로 이렇게 사용하는것이다.
* 따라서 필자도 날짜를 삽입하기로 마음먹었고, 또한 `int`형으로 삽입하도록 설계하였다.
* 여러 날짜 생성방법 중 `unix`의 `timestamp` 형식을 사용하기로 하였다.
* 사람이 눈으로 확인하기 보다는 컴퓨터가 정렬할 것이기 때문에 걱정없이 삽입하였다.
* 이 `unix timestamp`의 경우 시간이 지날수록 숫자가 커진다. `auto_increment`와 비슷하다고 생각하면된다.
* 사용법도 똑같이 `desc, asc`하여 사용하면된다.
* 이 방법은 `auto_increment`처럼 종속되지도 않고, 캐쉬를 이용해 기억할 필요없이 그냥 아래 함수를 이용해 값을 뽑아내면된다.
```kotlin
fun getCurrentTimestamp(): Int {
    return Instant.now().epochSecond.toInt()
}
```

## 조회쿼리가 이상하다
* 복합키를 이용해 조회하게되면 조금 이상한 점을 발견할 수 있다. 필자는 너무나 놀랐다.
* jpa에서 기본으로 지원하는 `findBy`와 같은 메서드를 잘 사용하지 않는데, 동작방식이 궁금해서 사용을 해보았다.
* 해당 메서드를 사용하려면 복합키를 넣어서 사용하게된다.
* 쿼리 로그는 아래처럼 찍혀나온다. 
```sql
where (
s1_0.followee_uuid,s1_0.follower_uuid
) in ((?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?))
```
* 데체.. 무슨일인가. 대략 50개정도되는 조건에 in을 걸고 있다. 딱 보아도 성능이 좋지 않아보인다.
* 괜찮다. 필자의 경우 jpa 기본 메서드는 `save`와 `delete` 밖에 사용하지 않아서 기존처럼 조회쿼리를 작성하여 사용하면된다.
* 그러나 위에서 설명하였듯 `id1 and id2`로 조회할때 순서를 반드시 따져야한다. 순서가 잘못되면 인덱스를 타지 않으니 말이다.
* 아래는 `jdsl`과 `QueryDsl`로 작성된 코드이다. 순서는 followeeUUID -> followerUUID를 지켜서 작성하였다.
```kotlin
//jdsl
override fun findSubscribeById(followeeUUID: UUID, followerUUID: UUID): Subscribe {
        return try {
            queryFactory.singleQuery {
                select(entity(Subscribe::class))
                from(Subscribe::class)
                where(col(Subscribe::followeeUUID).equal(followeeUUID).and(col(Subscribe::followerUUID).equal(followerUUID)))
            }
        } catch (e: NoResultException) {
            throw SubscribeException(SubscribeExceptionMessage.SUBSCRIBE_IS_NULL)
        }
}

//QueryDsl
override fun findSubscribeById(followeeId: UUID, followerId: UUID): Subscribe {
    return jpaQueryFactory.selectFrom(subscribe)
        .where(subscribe.followeeId.eq(followeeId).and(subscribe.followerId.eq(followerId)))
        .fetchOne() ?: throw SubscribeException(SubscribeExceptionMessage.SUBSCRIBE_IS_NULL, followerId)
}
```
* 이렇게 커스텀 쿼리를 자주 이용하는 사람이라면, 위에서 본 기괴한 쿼리 로그를 보지 않을 수 있다.