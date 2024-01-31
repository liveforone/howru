package howru.howru.jwt.service

import howru.howru.exception.exception.JwtCustomException
import howru.howru.exception.message.JwtExceptionMessage
import howru.howru.jwt.filterLogic.JwtTokenProvider
import howru.howru.jwt.domain.RefreshToken
import howru.howru.jwt.dto.ReissuedTokenInfo
import howru.howru.jwt.log.JwtServiceLog
import howru.howru.jwt.repository.RefreshTokenRepository
import howru.howru.logger
import howru.howru.member.domain.Role
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class JwtTokenService @Autowired constructor(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtTokenProvider: JwtTokenProvider
) {
    @Transactional(readOnly = true)
    fun getRefreshToken(id: UUID): RefreshToken {
        return refreshTokenRepository.findById(id).orElseThrow { logger().warn(JwtServiceLog.NOT_EXIST_REFRESH_TOKEN + id); throw JwtCustomException(JwtExceptionMessage.NOT_EXIST_REFRESH_TOKEN) }
    }

    fun createRefreshToken(id: UUID, refreshToken: String) {
        refreshTokenRepository.save(RefreshToken.create(id, refreshToken))
    }

    fun reissueToken(id: UUID, refreshToken: String, role: Role): ReissuedTokenInfo {
        jwtTokenProvider.validateToken(refreshToken)
        refreshTokenRepository.findById(id)
            .orElseThrow { logger().warn(JwtServiceLog.NOT_EXIST_REFRESH_TOKEN + id); throw JwtCustomException(JwtExceptionMessage.NOT_EXIST_REFRESH_TOKEN) }
            .let {
                check(it.refreshToken.equals(refreshToken)) { logger().warn(JwtServiceLog.UN_MATCH_REFRESH_TOKEN + id); throw JwtCustomException(JwtExceptionMessage.UN_MATCH_REFRESH_TOKEN) }

                val reissueToken = jwtTokenProvider.reissueToken(id, role)
                it.reissueRefreshToken(reissueToken.refreshToken)
                return reissueToken
            }
    }

    fun clearRefreshToken(id: UUID) {
        refreshTokenRepository.findById(id)
            .orElseThrow { logger().warn(JwtServiceLog.NOT_EXIST_REFRESH_TOKEN + id); throw JwtCustomException(JwtExceptionMessage.NOT_EXIST_REFRESH_TOKEN) }
            .also { it.clearToken() }
    }

    fun removeRefreshToken(id: UUID) {
        refreshTokenRepository.findById(id)
            .orElseThrow{ logger().warn(JwtServiceLog.NOT_EXIST_REFRESH_TOKEN + id); throw JwtCustomException(JwtExceptionMessage.NOT_EXIST_REFRESH_TOKEN) }
            .also { refreshTokenRepository.delete(it) }
    }
}