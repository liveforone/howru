package howru.howru.globalConfig.redis

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

class RedisKeyValueTimeOut(
    val time: Long,
    val timeUnit: TimeUnit
)

@Component
class RedisRepository(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val objectMapper: ObjectMapper
) {
    fun <T> save(
        key: String,
        value: T,
        timeOut: RedisKeyValueTimeOut? = null
    ) {
        timeOut?.let {
            redisTemplate.opsForValue().set(
                key,
                objectMapper.writeValueAsString(value),
                timeOut.time,
                timeOut.timeUnit
            )
        } ?: run {
            redisTemplate.opsForValue().set(
                key,
                objectMapper.writeValueAsString(value)
            )
        }
    }

    fun delete(key: String) {
        redisTemplate.delete(key)
    }

    fun <T> getByKey(
        key: String,
        clazz: Class<T>
    ): T? {
        val result = redisTemplate.opsForValue()[key].toString()
        return if (result.isEmpty()) {
            null
        } else {
            return objectMapper.readValue(result, clazz)
        }
    }

    operator fun <T> get(
        key: String,
        clazz: Class<T>
    ): T? {
        return getByKey(key = key, clazz = clazz)
    }

    operator fun <T> set(
        key: String,
        value: T
    ) {
        return save(key = key, value = value)
    }
}
