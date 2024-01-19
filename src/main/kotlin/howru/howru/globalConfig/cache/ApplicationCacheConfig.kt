package howru.howru.globalConfig.cache

import com.github.benmanes.caffeine.cache.Caffeine
import howru.howru.globalConfig.cache.constant.CacheName
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Arrays
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors

@Configuration
@EnableCaching
class ApplicationCacheConfig {

    //기본 캐시 사용 config
//    @Bean
//    fun cacheManager(): CacheManager {
//        return ConcurrentMapCacheManager().also { it.setCacheNames(listOf(CacheName.MEMBER, CacheName.POST, CacheName.ADVERTISEMENT)) }
//    }

    @Bean
    fun cacheManager(): CacheManager {
        val caches: MutableList<CaffeineCache> = Arrays.stream(CacheType.entries.toTypedArray())
            .map { cache -> CaffeineCache(cache.cacheName, Caffeine.newBuilder()
                .recordStats()
                .expireAfterWrite(cache.expiredAfterWrite, TimeUnit.HOURS)
                .maximumSize(cache.maximumSize)
                .build()
            ) }.collect(Collectors.toList())

        val cacheManger = SimpleCacheManager()
        cacheManger.setCaches(caches)
        return  cacheManger
    }
}