package howru.howru.globalConfig.cache

import howru.howru.globalConfig.cache.constant.CacheName
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableCaching
class ApplicationCacheConfig {
    @Bean
    fun cacheManager(): CacheManager {
        return ConcurrentMapCacheManager().also { it.setCacheNames(listOf(CacheName.MEMBER, CacheName.POST, CacheName.ADVERTISEMENT)) }
    }
}