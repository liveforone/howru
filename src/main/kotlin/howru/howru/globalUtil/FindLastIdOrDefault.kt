package howru.howru.globalUtil

fun findLastIdOrDefault(foundData: List<Any>): Long {
    val length = foundData.size
    if (length == 0) {
        return 0
    }
    val lastIndex = length - 1
    val lastData = foundData[lastIndex]
    return if (lastData is Map<*, *>) {
        (lastData["id"] as? Long) ?: 0
    } else {
        0
    }
}