package howru.howru.global.util

fun extractKeywords(text: String): String? {
    // 불용어정의 - 100개
    val stopWords =
        listOf(
            "이", "나오", "있", "가지", "하", "씨", "것", "시키", "들", "만들",
            "그", "지금", "되", "생각하", "수", "그러", "이", "속", "보", "하나",
            "않", "집", "없", "살", "나", "모르", "사람", "적", "주", "월",
            "아니", "데", "등", "자신", "안", "어떤", "때", "년", "가", "한",
            "지", "대하", "오", "말", "일", "그렇", "위하", "때문", "그것", "두",
            "말하", "알", "그러나", "받", "못하", "일", "그런", "또", "문제", "더",
            "사회", "많", "그리고", "좋", "크", "따르", "중", "놓"
        )

    // 단어 빈도를 계산합니다.
    val words = text.split("\\s+".toRegex()).filter { it !in stopWords && it.isNotBlank() }
    val wordCounts = words.groupingBy { it }.eachCount()
    return wordCounts.maxByOrNull { it.value }?.key
}
