package howru.howru.globalConfig.redis.constant

object RedisConstant {
    const val REDIS_URL = "\${spring.redis.url}"
    const val REFLECTION_CACHE_SIZE = 512
    const val TTL: Long = 15
}