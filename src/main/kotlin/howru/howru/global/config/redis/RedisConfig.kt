package howru.howru.global.config.redis

import howru.howru.global.config.redis.constant.RedisConstant
import io.lettuce.core.RedisURI
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.*

@Configuration
@EnableCaching
class RedisConfig(
    @Value(RedisConstant.REDIS_URL)
    val url: String
) {
    @Bean
    fun lettuceConnectionFactory(): LettuceConnectionFactory {
        val redisURI: RedisURI = RedisURI.create(url)
        val configuration = LettuceConnectionFactory.createRedisConfiguration(redisURI)
        return LettuceConnectionFactory(configuration)
    }

    @Bean
    fun redisTemplate(): RedisTemplate<String, Any> =
        RedisTemplate<String, Any>().apply {
            this.connectionFactory = lettuceConnectionFactory()
            this.keySerializer = StringRedisSerializer()
            this.valueSerializer = StringRedisSerializer()
            this.hashKeySerializer = StringRedisSerializer()
            this.hashValueSerializer = StringRedisSerializer()
        }
}
