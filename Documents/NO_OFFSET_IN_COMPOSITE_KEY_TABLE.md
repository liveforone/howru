# 복합키를 사용하는 테이블에서 timestamp 기반 no-offset 페이징
> 예제는 jdsl v2와 쿼리 dsl을 사용합니다.

## Jdsl 버전
* `limit` 함수를 지원하지 않는 `jdsl v3`에서는 사용이 불가합니다.
* v3에서는 `PageRequest`를 사용한 페이징만 가능합니다.
* 반드시 `jdsl v2` 버전 혹은 `QueryDsl` 에서만 사용하시기 바랍니다.

## 복합키를 사용하는 테이블에서 페이징
* 현재 모든 프로젝트, 그리고 해당 프로젝트에 `no-offset` 페이징을 적용하고 있습니다.
* 이는 `no-offset` 페이징의 성능 때문인데요, no-offset 페이징은 `auto increment pk`를 기반으로 동작합니다.
* 복합키는 auto increment pk가 없습니다. 두개의 키로 pk를 구성하고 있습니다.
* 이러한 복합키는 정렬이 어렵다는 단점때문에 [JPA에서 복합키로 조회 쿼리 최적화](https://github.com/liveforone/howru/blob/master/Documents/COMPOSITE_KEY_IN_JPA.md) 에서도 보실 수 있듯이
* 복합키에는 `timestamp` 라는 int형 `unix timestamp`를 넣어서 사용하고 있습니다.

## 어떻게 timestamp 기반으로 페이징을 처리할까?
* `unix`의 `timestamp`는 `auto increment`와 유사하게 뒤로 가면 갈수록 값이 커지는 특징이 있습니다.
* 이러한 특징을 이용하면 기존의 id와 동일하게 `desc, asc` 등의 정렬을 할 수 있습니다.
* 또한 크고 작음을 따질 수 있음으로 `no offset`페이징 또한 구현이 가능합니다.

## 코드로 보기
* 일반적인 `no offset`과 동일합니다. 다만 마지막 데이터의 `timestamp`를 매개변수로 받아서 처리한다는 것이 유일한 차이점입니다.
* 이 매개변수는 `null`이 가능하며, `null`값이 입력될 경우 첫번째 페이지로 취급합니다.
* 또한 클라이언트의 잘못된 입력을 방지하기 위해 상식으로 `timestamp`값이 될 수 없는 0보다 작은 값이 들어왔을 때에도 첫번째 페이지를 리턴해줍니다.
```kotlin
//kotlin-jdsl
private fun <T> SpringDataCriteriaQueryDsl<T>.ltLastTimestamp(lastTimestamp: Int?): PredicateSpec? {
        return lastTimestamp?.takeIf { it > 0 }?.let { and(col(Subscribe::timestamp).lessThan(it)) }
}

//QueryDsl
private fun ltTimestamp(lastTimestamp: Int?): BooleanExpression? =
    lastTimestamp?.takeIf { it > 0 }?.let { subscribe.timestamp.lt(it) }

//글로벌 유틸함수로 선언한 QueryDsl 제네릭 함수
fun <T, Q : EntityPathBase<T>> ltLastTimestamp(
    lastTimestamp: Int?,
    qEntity: Q,
    timestampPathExtractor: (Q) -> NumberPath<Int>
): BooleanExpression? {
    return lastTimestamp?.takeIf { it > 0 }?.let { timestampPathExtractor(qEntity).lt(it) }
}
```

## 결론
* 일반적인 `no-offset`과 별반 다르지 않습니다.
* 그러나 `auto increment pk`가 존재하지 않는 복합키에서 `no-offset` 페이징을 구현하려하면 난감해질 수 있습니다.
* `timestamp`라는 아이디어를 이용해 복합키에서도 no-offset 페이징 문제를 해결하시기 바랍니다.
