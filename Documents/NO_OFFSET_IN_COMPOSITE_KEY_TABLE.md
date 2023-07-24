# 복합키를 사용하는 테이블에서 timestamp 기반 no-offset 페이징

## 복합키를 사용하는 테이블에서 페이징
* 현재 모든 프로젝트, 그리고 해당 프로젝트에 no-offset 페이징을 적용하고 있습니다.
* 이는 no-offset 페이징의 성능 때문인데요, no-offset 페이징은 auto increment pk를 기반으로 동작합니다.
* 복합키는 auto increment pk가 없습니다. 두개의 키로 pk를 구성하고 있습니다.
* 이러한 복합키는 정렬이 어렵다는 단점때문에 [JPA에서 복합키로 조회 쿼리 최적화](https://github.com/liveforone/howru/blob/master/Documents/COMPOSITE_KEY_IN_JPA.md) 에서도 보실 수 있듯이 
* 복합키에는 timestamp 라는 int형 unix timestamp를 넣어서 사용하고 있습니다.

## 어떻게 timestamp 기반으로 페이징을 처리할까?
* unix의 timestamp는 auto increment와 유사하게 뒤로 가면 갈수록 값이 커지는 특징이 있습니다.
* 따라서 id와 동일하게 생각하고 desc, asc 등의 정렬을 하기에 아주 편리합니다.
* 이러한 점을 이용해서 uuid를 사용할때 no-offset 페이징을 하는 것과 동일 하게 처리하면됩니다.

## 코드로 보기
* 일단 lastUUID를 이용해서 timestamp를 찾아야합니다.
* criteria에서 singleQuery는 noresult exception이 발생할 수 있기 때문에 예외처리를 반드시 해줍니다.
* 특히나 int 리턴 타입에서 자주 발생합니다.
* 페이징을 하다보면 맨 마지막 데이터에 접근할 수도 있습니다. 맨 마지막 데이터를 바탕으로 no-offset 페이징을 처리하게 되면
* 반드시 no result exception에러가 발생합니다. 따라서 맨 마지막 데이터임을 전달 하도록 0을 전달하여(id도 1부터 시작하고, timestamp도 0은 없습니다.) 마지막 데이터임을 표시하여 줍니다.
* 파라미터는 null이 허용됩니다. 첫 페이지의 경우 lastUUID가 없기 때문입니다.
* 예제에서는 두개의 파라미터를 받아 null을 체크합니다.
* 그리곤 실제 쿼리의 조건절(where)에 넣어 쿼리를 날리도록 하였습니다.
```kotlin
private fun findLastTimestamp(lastFolloweeUUID: UUID, lastFollowerUUID: UUID): Int {
        return try {
            queryFactory.singleQuery {
                select(listOf(col(Subscribe::timestamp)))
                from(Subscribe::class)
                where(
                    col(Subscribe::followeeUUID).equal(lastFolloweeUUID)
                        .and(col(Subscribe::followerUUID).equal(lastFollowerUUID))
                )
            }
        } catch (e: NoResultException) {
            SubscribeRepoConstant.END_OF_TIMESTAMP  // = 0
        } 
}

private fun <T> SpringDataCriteriaQueryDsl<T>.ltLastTimestamp(lastFolloweeUUID: UUID?, lastFollowerUUID: UUID?): PredicateSpec? {
        val findTimestamp = lastFolloweeUUID?.let { followee ->
            lastFollowerUUID?.let { follower ->
                findLastTimestamp(followee, follower)
            }
        }
        return findTimestamp?.let { and(col(Subscribe::timestamp).lessThan(it)) }
}

override fun findSubscribesByFollowee(followeeUUID: UUID, lastFolloweeUUID: UUID?, lastFollowerUUID: UUID?): List<SubscribeInfo> {
    return queryFactory.listQuery {
        select(listOf(
            col(Subscribe::followeeUUID),
            col(Subscribe::followerUUID)
        ))
        from(Subscribe::class)
        where(col(Subscribe::followeeUUID).equal(followeeUUID))
        where(ltLastTimestamp(lastFolloweeUUID, lastFollowerUUID))
        orderBy(col(Subscribe::timestamp).desc())
        limit(SubscribeRepoConstant.LIMIT_PAGE)
    }
}
```

## 결론
* uuid를 이용한 no-offset과 별반 다르지 않습니다.
* 복합키를 자주 사용하다보면 페이징할일이 반드시 생깁니다.
* 이때 no-offset으로 구현하고 싶은경우 위의 예제를 참고하여 구현하시면 되겠습니다.