package howru.howru.jwt.filterLogic

import howru.howru.jwt.exception.JwtCustomException
import howru.howru.jwt.exception.JwtExceptionMessage
import howru.howru.jwt.constant.JwtConstant
import howru.howru.jwt.dto.JwtTokenInfo
import howru.howru.logger
import howru.howru.member.domain.Role
import io.jsonwebtoken.*
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider {
    private val key = Jwts.SIG.HS256.key().build()

    fun generateToken(authentication: Authentication): JwtTokenInfo {
        val id = UUID.fromString(authentication.name)
        return JwtTokenInfo.create(id, generateAccessToken(authentication), generateRefreshToken())
    }

    private fun generateAccessToken(authentication: Authentication): String {
        return Jwts.builder()
            .subject(authentication.name)
            .claim(JwtConstant.CLAIM_NAME, authentication.authorities.iterator().next().authority)
            .expiration(Date(Date().time + JwtConstant.TWO_HOUR_MS))
            .signWith(key)
            .compact()
    }

    fun reissueToken(
        id: UUID,
        role: Role
    ): JwtTokenInfo {
        return JwtTokenInfo.create(id, generateAccessTokenWhenReissue(id, role), generateRefreshToken())
    }

    private fun generateAccessTokenWhenReissue(
        id: UUID,
        role: Role
    ): String {
        return Jwts.builder()
            .subject(id.toString())
            .claim(JwtConstant.CLAIM_NAME, role.auth)
            .expiration(Date(Date().time + JwtConstant.TWO_HOUR_MS))
            .signWith(key)
            .compact()
    }

    private fun generateRefreshToken(): String {
        return Jwts.builder()
            .expiration(Date(Date().time + JwtConstant.THIRTY_DAY_MS))
            .signWith(key)
            .compact()
    }

    fun getAuthentication(accessToken: String): Authentication {
        val claims = parseClaims(accessToken)
        val authorities: Collection<GrantedAuthority> =
            claims[JwtConstant.CLAIM_NAME].toString()
                .split(JwtConstant.AUTH_DELIMITER)
                .map { role: String? -> SimpleGrantedAuthority(role) }
        val principal: UserDetails = User(claims.subject, JwtConstant.EMPTY_PW, authorities)
        return UsernamePasswordAuthenticationToken(principal, JwtConstant.CREDENTIAL, authorities)
    }

    private fun parseClaims(accessToken: String): Claims {
        return try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(accessToken).payload
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }

    fun validateToken(token: String) {
        if (token.isBlank()) {
            throw JwtCustomException(JwtExceptionMessage.TOKEN_IS_NULL)
        }
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
        } catch (e: MalformedJwtException) {
            logger().info(JwtExceptionMessage.INVALID_TOKEN.message)
            throw JwtCustomException(JwtExceptionMessage.EMPTY_CLAIMS)
        } catch (e: ExpiredJwtException) {
            logger().info(JwtExceptionMessage.EXPIRED_JWT_TOKEN.message)
            throw JwtCustomException(JwtExceptionMessage.EXPIRED_JWT_TOKEN)
        } catch (e: UnsupportedJwtException) {
            logger().info(JwtExceptionMessage.UNSUPPORTED_TOKEN.message)
            throw JwtCustomException(JwtExceptionMessage.UNSUPPORTED_TOKEN)
        } catch (e: SecurityException) {
            logger().info(JwtExceptionMessage.INVALID_TOKEN.message)
            throw JwtCustomException(JwtExceptionMessage.INVALID_TOKEN)
        }
    }
}
