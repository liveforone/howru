# 대용량 IN 쿼리 성능 향상 및 주의점

## 대용량 IN 쿼리란?
* 대용량 IN 쿼리는 IN 절에 상당히 많은 양의 데이터를 넣는것을 의미한다.
* 필자는 이런 의문이 들었다. 
* "IN 절에 들어가는 데이터가 굉장히 많아지면 성능이 급격히 하락하지 않을까?"

## IN절 인덱스
* 문제에 들어가기에 앞서서 `in`절의 인덱스에 대해 알아보도록 한다.
* `between`, `like`, `<`, `>` 등 범위 조건의 경우 범위 조건에 해당하는 컬럼은 인덱스를 타지만, 
* 그 뒤 인덱스 컬럼들은 인덱스가 사용되지 않는다.
* 쉽게말해 `(a,b,c)`컬럼들에 순서대로 인덱스가 걸려있는 상황에서 조회 쿼리를 `where a=값1 and b > 값2 and c=값3` 으로 쿼리를 날리면 c는 인덱스가 사용되지 않는다.
* b에서 범위 조건이 걸렸기 때문에 a와 b까지는 인덱스를 사용하고 c는 인덱스를 타지 않는다는 것이다.
* 그러나 in절은 `=` 과 마찬가지로 다음 컬럼도 인덱스를 사용한다.
* 따라서 조회할 모든 컬럼에 대한 인덱스를 걸고, 조건과 순서를 맞춰서 조회해야한다.

## IN 안에 데이터 수는 제한이 존재한다.
* mysql 8.0 기준 in절 안에는 조건 200개가 제한이다.
* 이는 `eq_range_index_dive_limit` 으로 표시된다.
* 이와 함께 메모리 제한 옵션도 존재하는데, `range_optimizer_max_mem_size` 으로 표시된다.
* 200개를 넘어가게되면 `인덱스 다이브` 방식이 아니라 `인덱스 통계`를 사용한다.
* 따라서 성능저하는 반드시 발생한다.
* 결과적으로 이 옵션들은 아예 꺼버려서 문제를 예방하는 것이 좋다
* 끄지 않는다면 파라미터 갯수를 잘 맞추어서 파라미터를 넣어주는 것이 필요할것이다.
* 아래에서 후술하겠지만 그런 경우가 상당히 까다롭고 불가피한 상황이 있다. 따라서 옵션을 꺼주는 것이 좋다.
* 옵션은 `mysql`기준 아래 커맨드를 사용해서 끌 수 있다.
```sql
set eq_range_index_dive_limit = 0;
set range_optimizer_max_mem_size = 0;
```

## 파라미터 갯수를 맞추는 것이 불가능한 상황
* 아래에 코드도 파라미터 갯수를 완벽히 맞춘 코드는 아니다.
* 파라미터 갯수를 맞추기 위해서는 입력받은 in 쿼리 파라미터를 200개씩 잘라서 
* union 쿼리를 이용해 쿼리를 날리고 병합해주면 끝난다.
* 그러나 이것이 불가능 한 경우가 있다. 바로 아래처럼 `no-offset` 기반의 페이징을 할 때이다.
* 200개씩 잘라서 4그룹이 나오게되면 4그룹은 모두 4개의 lastID를 가지게 된다.
* 이것을 관리하는 것도 상당히 어려운 일이다.
* 필자의 경험을 삼아 볼때 no-offset 페이징을 하는 경우 파라미터 갯수를 맞추는 것은 사실상 불가능하다.
```kotlin
override fun findPostsByFollowee(followeeUUID: List<UUID>, lastUUID: UUID?): List<PostInfo> {
        val resultPost: MutableList<PostInfo> = mutableListOf()
        if (followeeUUID.size >= 200) {
            val dividedFollowee: List<List<UUID>> = followeeUUID.chunked(200)
            for (subList in dividedFollowee) {
                resultPost.addAll(findPostsEachLimitInQuerySize(followeeUUID, lastUUID))
            }
        } else {
            resultPost.addAll(findPostsEachLimitInQuerySize(followeeUUID, lastUUID))
        }
        return resultPost
    }

    private fun findPostsEachLimitInQuerySize(limitInQuerySizeUUID: List<UUID>, lastUUID: UUID?): List<PostInfo> {
        return queryFactory.listQuery {
            select(listOf(
                col(Post::uuid),
                col(Member::uuid),
                col(Post::content),
                col(Post::createdDate)
            ))
            from(Post::class)
            join(Post::writer)
            where(col(Member::uuid).`in`(limitInQuerySizeUUID))
            where(ltLastUUID(lastUUID))
            orderBy(col(Post::id).desc())
            limit(PostRepoConstant.LIMIT_PAGE)
        }
    }
```

## JPA, HIBERNATE 관점에서 살펴보기
* hibernate는 쿼리 컴파일 시간 단축을 위해 `query plan cache`를 사용한다.
* 그리고 하이버네이트는 캐시를 확인해서 쿼리플랜은 찾는다.
* 만약 캐시에 쿼리 플랜이 존재한다면 재사용을 할것이다.
* in절을 사용하다보면 쿼리플랜 캐시가 과도하게 메모리를 사용하는 것을 볼 수 있다.
* 따라서 이러한 문제를 해결하기 위해 `in_clause_parameter_padding` 옵션을 true로 설정하는 것이 좋다.
* 이 설정은 여러 In절 쿼리에서 IN절 Query Plan Cache을 재사용할 수 있게 해준다. 
* 매개변수가 달라도, 동일한 In절 쿼리를 사용하게 함으로써 성능을 올린다. 
* IN 절 쿼리에 대해서 2의 제곱단위로 쿼리를 생성한다. 
* 따라서 1000개가 될 캐시 쿼리를 10개로, 3만개가 될 캐시 쿼리를 15개로 줄여준다.
```
hibernate.query.in_clause_parameter_padding=true
```
### 문제
* 문제는 2의 제곱단위로 쿼리를 생성하기 때문에 128개를 초과하게되면 200을 넘기게된다.
* 200을 넘기게되면 DB에서 아무 설정을 하지 않았을 경우 인덱스 통계를 사용하게 되고 이때부터 성능이 하락한다.
* 따라서 DB에서 아무 설정을 하지 않을경우, in 절에 들어가는 값이 128개를 초과하지 않도록 해야한다.

## 결론
* 언제나 인덱스 다이빙을 사용하도록 설정할것을 필자는 권유한다.
* 따라서 DB에서 `eq_range_index_dive_limit`와 `range_optimizer_max_mem_size`의 옵션을 off로 설정하고
* hibernate에서 쿼리 플랜 캐시를 최적화하는 `hibernate.query.in_clause_parameter_padding` 를 on하여 최적화할 것을 권장한다.
* 이렇게 옵션을 설정할 경우 in절의 갯수에 대한 스트레스/관리가 없어서 좋다.
* 아래는 변경할 옵션에 대한 최종 코드이다.
### DB
```sql
set eq_range_index_dive_limit = 0;
set range_optimizer_max_mem_size = 0;
```
### hibernate -> application.yml
```yaml
hibernate.query.in_clause_parameter_padding=true
```