# 코드만으로 키워드 추출

## 키워드 추출 방법
* 해당 프로젝트에서 키워드 추출 목적은 바닐라, 즉 오로지 코드만으로 키워드를 추출하는 것이다.
* 키워드 추출하는 방법으로 빈도 기반 추출을 사용했다.

## 빈도 기반 추출 방법
* 문장을 공백을 기준으로 분리한다.
* 불용어를 미리 정의하고 불용어 해당하지 않는 문장만 꺼낸다.
* 여기에서는 불용어 100개를 정의했다.
* 최다 빈도의 한개의 키워드로 검색을 진행한다.
* 다만 쿼리를 날리때 in절을 사용하면 안되고 %like%를 사용해야한다.
* 다만 성능상의 문제가 있으므로 최신 데이터 100개중 5개를 뽑아오는 방법으로 한다.
```kotlin
fun extractKeywords(text: String): String? {
    //불용어정의 - 100개
    val stopWords = listOf(
        "이", "나오", "있", "가지", "하", "씨", "것", "시키", "들", "만들",
        "그", "지금", "되", "생각하", "수", "그러", "이", "속", "보", "하나",
        "않", "집", "없", "살", "나", "모르", "사람", "적", "주", "월",
        "아니", "데", "등", "자신", "안", "어떤", "때", "년", "가", "한",
        "지", "대하", "오", "말", "일", "그렇", "위하", "때문", "그것", "두",
        "말하", "알", "그러나", "받", "못하", "일", "그런", "또", "문제", "더",
        "사회", "많", "그리고", "좋", "크", "따르", "중", "놓"
    )

    //단어 빈도를 계산합니다.
    val words = text.split("\\s+".toRegex()).filter { it !in stopWords && it.isNotBlank() }
    val wordCounts = words.groupingBy { it }.eachCount()
    return wordCounts.maxByOrNull { it.value }?.key
}
```

## 최근 100개의 데이터중 5개 뽑아오는 쿼리
* 쿼리로 나타내면 아래와 같다.
```sql
SELECT *
FROM table_name
ORDER BY id DESC
LIMIT 100
OFFSET 0 -- OFFSET 값은 필요에 따라 변경 가능
LIMIT 5;
```
### JDSL로 정의
* `PostRepoConstant.NEWEST_LIMIT_PAGE = 100`
* `PostRepoConstant.START_OFFSET = 0`
* `PostRepoConstant.RECOMMEND_LIMIT_PAGE = 5`
```kotlin
override fun findRecommendPosts(keyword: String?): List<PostInfo> {
        return queryFactory.listQuery {
            select(listOf(
                col(Post::uuid),
                col(Member::uuid),
                col(Post::content),
                col(Post::createdDate)
            ))
            from(Post::class)
            join(Post::writer)
            where(dynamicKeywordSearch(keyword))
            orderBy(col(Post::id).desc())
            limit(PostRepoConstant.NEWEST_LIMIT_PAGE)
            offset(PostRepoConstant.START_OFFSET)
            limit(PostRepoConstant.RECOMMEND_LIMIT_PAGE)
        }
}

private fun <T> SpringDataCriteriaQueryDsl<T>.dynamicKeywordSearch(keyword: String?): PredicateSpec? {
    return keyword?.let { and(col(Post::content).like("%$keyword%")) }
}
```