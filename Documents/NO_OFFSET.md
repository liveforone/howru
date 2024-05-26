# No Offset 페이징으로 페이징 성능 향상

## 목차

- [No Offset 페이징으로 페이징 성능 향상](#no-offset-페이징으로-페이징-성능-향상)
    - [목차](#목차)
    - [성능 향상을 위한 기본적인 전략](#성능-향상을-위한-기본적인-전략)
    - [병목 처리 팁](#병목-처리-팁)
    - [일반적인 페이징](#일반적인-페이징)
    - [No Offset 페이징](#no-offset-페이징)
    - [구현 방법](#구현-방법)
    - [QueryDsl 구현](#querydsl-구현)
    - [글로벌 유틸 제네릭 함수로 선언](#글로벌-유틸-제네릭-함수로-선언)
    - [결론](#결론)

## 성능 향상을 위한 기본적인 전략

- 성능을 향상하기 위해서 기본적으로 인덱스는 걸려있어야합니다.
- 또한 쿼리가 인덱스를 잘 타고 있어야합니다. 실행계획 등을 통해서 쿼리가 인덱스를 잘 타고 있는지 반드시 확인해야합니다.
- 이런 기본적인 성능 향상 전략을 마친 후 페이징 성능을 개선하는 것이 올바릅니다.

## 병목 처리 팁

- 성능 문제는 일반적으로 단건 조회보다는 컬렉션 조회(여러건의 데이터 조회)에서 많이 발생합니다.
- 특히 컬렉션 조회, 그 중에서 페이징 같은 경우에는 캐싱을 하기 어렵습니다.
- 자주 바뀌는 특성을 지녔기 때문에, 캐시를 사용하는 것이 어렵습니다.
- 데이터가 자주 바뀌지 않아야한다는 기본적인 캐시 사용 조건을 충족하지 않기 때문입니다.
- 따라서 성능을 개선하고 병목을 처리할때에는 페이징처럼 컬렉션 조회에 초점을 맞추는 것이 좋습니다.
- 문제를 일으키는 가장 일반적인 원인이기 때문입니다.

## 일반적인 페이징

- 일반적인 페이징은 `offset`을 사용합니다. `offset`에는 페이지 번호가 들어갑니다.
- 더 정확히 설명하자면 페이지 번호를 가지고 다음과 같은 방식으로 `offset`을 구합니다.
- `(page-1) * limit_size`
- 이런 식의 페이징은 `offset크기 + limit크기` 만큼의 데이터를 조회해야합니다.
- 그리고 앞의 `offset`크기만큼의 데이터를 버립니다.
- `limit`크기만큼의 데이터만 필요하기 때문입니다.
- 일례로 `offset`이 1000이고, `limit`이 10이라면 1000 + 10 개만큼의 데이터를 조회하고
- 앞의 1000개의 데이터를 버립니다. 그리고 10개만 사용합니다.
- 즉 이런 식의 쿼리는 항상 이전의 모든 데이터를 읽고 버린 후 `limit`개만 반환하겠구나라고 예상할 수 있습니다.
- 이러한 특성 때문에 뒤의 페이지로 가면 갈수록 페이징 성능이 떨어집니다.
- 또한 필요한 데이터만큼 조회하지 않기 때문에 상당히 비효율적입니다.

## No Offset 페이징

- No Offset 페이징은 이런 일반적인 페이징과 다르게 동작합니다.
- `offset`이 존재하지 않고, 이전에 조회한 데이터 중 가장 마지막 데이터의 식별자(ex : `id`)를 기준으로 다음에 올 데이터들을 찾아서 가져옵니다.
- 즉 데이터 조회의 시작 조건을 `offset`이 아닌 `pk`등을 사용하여 인덱스를 통해 빠르게 찾는 방식입니다.
- 데이터 조회 시작 조건을 빠르게 찾고 필요한 데이터만 가져올 수 있다는 것이 핵심입니다.
- 이러한 이유로 `No offset`페이징은 좋은 성능을 뽑아낼 수 있습니다.
- 쉽게 말해서 데이터 정렬기준이 `desc`이고, `auto increment pk`를 사용하고 있다고 가정하겠습니다.
- 그리고 이전에 조회한 데이터의 가장 마지막 `pk`는 5라고 한다면, 5보다 작은(less than) `pk`를 가진 데이터를 조회하여 `limit`사이즈에 맞춰서 가져옵니다.
- 마치 첫번째 페이지를 읽듯이 인덱스를 태워서 빠르게 컬렉션을 조회합니다.
- 이러한 특징 때문에 페이지가 뒤로 가면 갈수록 성능이 떨어지는 것이 아닌, 시작 위치가 어디가 됬던 첫번째 페이지를 조회하는 것과 같은 속도로 데이터를 조회할 수 있습니다.
- 계속 '첫번째 페이지'라는 워딩이 나오는데, 첫번째 페이지는 offset이 없습니다. offset을 사용한 쿼리에서 가장 효율적인 데이터는 첫번째 페이지입니다.
- 이러한 이유로 첫번째 페이지와 같다 라는 표현을 사용하고 있습니다.(이해에 어려움이 없길 바랍니다.)
- 쿼리로 보면 이해가 쉬워집니다. 쿼리는 아래와 같습니다

```sql
#lastId는 이전에 조회한 데이터 중 가장 마지막 데이터의 id입니다.
SELECT * FROM data WHERE 조건 AND id < lastId ORDER BY id DESC LIMIT 리밋사이즈
```

- 만약 asc 정렬을 사용한다면 다음과 같이 할 수 있습니다.

```sql
#firstId는 이전에 조회한 데이터 중 가장 첫번째 데이터의 id입니다.
SELECT * FROM data WHERE 조건 AND id > firstId ORDER BY id ASC LIMIT 리밋사이즈
```

## 구현 방법

- `desc`정렬을 사용하는 가정하에 설명하겠습니다.
- 클라이언트로부터 가장 마지막 데이터의 id를 받아와야합니다.
- 그러나 첫번째 페이지의 경우 데이터가 존재하지 않습니다.
- 이때는 아무 값을 받지 않아도 무방합니다.
- 쿼리에서는 아무 값도 받지 않았을 경우 where 조건 중 `lastId`보다 작은 데이터를 찾는 조건만 생략하고
- `limit`사이즈에 맞게 데이터를 뽑아오면 됩니다.
- 만약 `lastId`를 받았을 경우에는 `lastId`보다 작은 데이터만은 찾아서 `limit`사이즈 만큼 가져오면 됩니다.
- `pk`에는 기본적으로 인덱스가 걸려있습니다. 이런 조회 방식을 사용하면 인덱스를 태울 수 있습니다.
- 그러나 다른 조건이 필요하다면 해당 조건까지 포함할 수 있는 인덱스를 만드는 것이 좋습니다.

## QueryDsl 구현

- 쿼리Dsl로 구현한 코드는 아래와 같습니다.
- 쿼리Dsl의 `and`절에 `null`을 삽입하면 해당 조건을 무시합니다.
- 이러한 특징을 이용하여 첫번째 페이지를 대응할 수 있습니다.
- 첫번째 페이지는 클라이언트로부터 `lastId`를 받지 않습니다.
- 즉 클라이언트로부터 받아오는 `lastId`는 `null`입니다.
- 또한 `lastId`가 현실적으로 0이하의 값이 될 수 없으므로 0이하의 값이 들어올 경우에 대해서도 필터링 하였습니다.
- 이러한 상황에서는 함수가 `null`을 리턴하여 첫번째 페이지를 리턴하도록 합니다.
- 그 이외의 상황에서는 `lastId`보다 작은 `id`를 가진 데이터들만 조회하여 가져옵니다.
- 즉 아래 커스텀 함수는 다음과 같이 풀이할 수 있습니다.
- `lastId`가 `null`이면 `null`을 반환한다. 이를 통해 쿼리는 첫번째 페이지를 리턴한다.
- `lastId`가 0이하일 경우 `null`을 반환한다. 이 또한 쿼리가 첫번째 페이지를 리턴한다.
- 위의 두 조건에 모두 충족하지 않는 경우, 즉 `lastId`가 `null`이 아니고 0초과일 경우 `lastId`보다 작은 데이터를 조회하여 리턴한다.

```kotlin
override fun findAllPosts(lastId: Long?): List<PostInfo> {
    return jpaQueryFactory.select(
        Projections.constructor(
            PostInfo::class.java,
            post.id,
            post.writer.id,
            post.content,
            post.postState,
            post.createdDatetime
        ),
    )
        .from(post)
        .where(ltLastId(lastId))  //커스텀 함수를 호출하여 사용합니다.
        .orderBy(post.id.desc())
        .limit(PostRepoConstant.LIMIT_PAGE)
        .fetch()
}

//여러 조건이 있는 경우
override fun findPostsByWriter(memberId: UUID, lastId: Long?): List<PostInfo> {
    return jpaQueryFactory.select(
        Projections.constructor(
            PostInfo::class.java,
            post.id,
            post.writer.id,
            post.content,
            post.postState,
            post.createdDatetime
        ),
    )
        .from(post)
        .where(post.writer.id.eq(memberId).and(ltLastId(lastId))) //여러 조건이 있는 경우 마지막에 배치하였습니다.
        .orderBy(post.id.desc())
        .limit(PostRepoConstant.LIMIT_PAGE)
        .fetch()
}

private fun ltLastId(lastId: Long?): BooleanExpression? =
        lastId?.takeIf { it > 0 }?.let { post.id.lt(it) }
```

## 글로벌 유틸 제네릭 함수로 선언

- 글로벌 유틸 제네릭 함수로 선언하여 사용할 수도 있습니다.
- 글로벌 유틸 제네릭 함수를 선언하면 매 `repository`마다 함수를 선언하지 않아도 됩니다.
- 이 때문에 재사용성이 올라갑니다.

```kotlin
fun <T, Q : EntityPathBase<T>> ltLastId(
    lastId: Long?,
    qEntity: Q,
    idPathExtractor: (Q) -> NumberPath<Long>
): BooleanExpression? {
    return lastId?.takeIf { it > 0 }?.let { idPathExtractor(qEntity).lt(it) }
}
```

- 아래 예제는 제네릭 함수를 사용하는 방법입니다. 코틀린의 trailing lambda를 이용해서 표현합니다.
- `ltLastId(lastId, post) { it.id }` 여기서 `post`는 미리 선언된 `Qclass`를 의미합니다.

```kotlin
override fun findPostsByWriter(
        memberId: UUID,
        lastId: Long?
    ): PostPage {
        val postInfoList =
            jpaQueryFactory.select(
                Projections.constructor(
                    PostInfo::class.java,
                    post.id,
                    post.writer.id,
                    post.content,
                    post.postState,
                    post.createdDatetime
                )
            )
                .from(post)
                .where(post.writer.id.eq(memberId).and(ltLastId(lastId, post) { it.id }))
                .orderBy(post.id.desc())
                .limit(PostRepoConstant.LIMIT_PAGE)
                .fetch()

        return PostPage(postInfoList, findLastIdOrDefault(postInfoList) { it.id })
    }
```

## 결론

- 무조건 페이징을 할때 No offset 페이징을 사용할 필요가 없습니다.
- 무한 스크롤방식을 이용하거나, 기존 페이징 보다 최적화를 원할 때 사용하면 됩니다.
- 또한 여러 조건을 사용하는 경우 인덱스 병합이 잘 작동하는지 확인하고, 작동하지 않는다면 복합 인덱스를 새로 구성해야합니다.
