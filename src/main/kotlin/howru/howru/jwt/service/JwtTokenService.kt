package howru.howru.jwt.service

import howru.howru.jwt.exception.JwtCustomException
import howru.howru.jwt.exception.JwtExceptionMessage
import howru.howru.global.config.redis.RedisTimeOut
import howru.howru.global.config.redis.RedisRepository
import howru.howru.global.config.redis.constant.CacheTTL
import howru.howru.jwt.cache.JwtCacheKey
import howru.howru.jwt.domain.RefreshToken
import howru.howru.jwt.dto.JwtTokenInfo
import howru.howru.jwt.filterLogic.JwtTokenProvider
import howru.howru.jwt.log.JwtServiceLog
import howru.howru.logger
import howru.howru.member.domain.Role
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.TimeUnit

@Service
class JwtTokenService
    @Autowired
    constructor(
        private val redisRepository: RedisRepository,
        private val jwtTokenProvider: JwtTokenProvider
    ) {
        fun getRefreshToken(id: UUID): RefreshToken =
            redisRepository.getByKey(JwtCacheKey.REFRESH_TOKEN + id, RefreshToken::class.java)
                ?: throw JwtCustomException(JwtExceptionMessage.NOT_EXIST_REFRESH_TOKEN).apply {
                    logger().warn(JwtServiceLog.NOT_EXIST_REFRESH_TOKEN + id)
                }

        fun createRefreshToken(
            id: UUID,
            refreshToken: String
        ) {
            redisRepository.save(
                JwtCacheKey.REFRESH_TOKEN + id,
                RefreshToken.create(id, refreshToken),
                RedisTimeOut(CacheTTL.FIFTEEN, TimeUnit.DAYS)
            )
        }

        fun reissueToken(
            id: UUID,
            refreshToken: String,
            role: Role
        ): JwtTokenInfo {
            jwtTokenProvider.validateToken(refreshToken)
            val key = JwtCacheKey.REFRESH_TOKEN + id
            redisRepository.getByKey(key, RefreshToken::class.java)
                ?.let {
                    check(it.refreshToken.equals(refreshToken)) {
                        logger().warn(JwtServiceLog.UN_MATCH_REFRESH_TOKEN + id)
                        throw JwtCustomException(JwtExceptionMessage.UN_MATCH_REFRESH_TOKEN)
                    }
                    val reissueToken = jwtTokenProvider.reissueToken(id, role)
                    it.reissueRefreshToken(reissueToken.refreshToken)
                    redisRepository.save(key, it)
                    return reissueToken
                }
                ?: throw JwtCustomException(JwtExceptionMessage.NOT_EXIST_REFRESH_TOKEN).apply {
                    logger().warn(JwtServiceLog.NOT_EXIST_REFRESH_TOKEN + id)
                }
        }

        fun removeRefreshToken(id: UUID) {
            redisRepository.delete(JwtCacheKey.REFRESH_TOKEN + id)
        }
    }
