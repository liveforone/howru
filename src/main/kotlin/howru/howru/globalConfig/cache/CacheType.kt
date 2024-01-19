package howru.howru.globalConfig.cache

enum class CacheType(
    val cacheName: String,
    val expiredAfterWrite: Long,
    val maximumSize: Long
) {
    USER_CACHE("memberQueryCache", 2, 300),
    POST_CACHE("postQueryCache", 4, 800),
    AD_CACHE("advertisementQueryCache", 48, 300)
}