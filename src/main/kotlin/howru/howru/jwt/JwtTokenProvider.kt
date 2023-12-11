package howru.howru.jwt

import howru.howru.exception.exception.JwtCustomException
import howru.howru.exception.message.JwtExceptionMessage
import howru.howru.jwt.constant.JwtConstant
import howru.howru.logger
import howru.howru.jwt.dto.JwtTokenInfo
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider (
    @Value(JwtConstant.SECRET_KEY_PATH) private var secretKey: String
) {
    private val key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey))

    fun generateToken(authentication: Authentication): JwtTokenInfo {
        val uuid = UUID.fromString(authentication.name)
        return JwtTokenInfo.create(uuid, generateAccessToken(authentication), generateRefreshToken())
    }

    private fun generateAccessToken(authentication: Authentication): String {
        return Jwts.builder()
            .setSubject(authentication.name)
            .claim(
                JwtConstant.CLAIM_NAME,
                authentication.authorities.iterator().next().authority
            )
            .setExpiration(Date(Date().time + JwtConstant.TWO_HOUR_MS))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    private fun generateRefreshToken(): String {
        return Jwts.builder()
            .setExpiration(Date(Date().time + JwtConstant.THIRTY_DAY_MS))
            .signWith(key, SignatureAlgorithm.HS256)
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
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .body
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }

    fun validateToken(token: String): Boolean {
        if (token.isBlank()) {
            throw JwtCustomException(JwtExceptionMessage.TOKEN_IS_NULL)
        }
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            return true
        } catch (e: MalformedJwtException) {
            logger().info(JwtExceptionMessage.INVALID_MESSAGE.message)
            throw JwtCustomException(JwtExceptionMessage.EMPTY_CLAIMS)
        } catch (e: ExpiredJwtException) {
            logger().info(JwtExceptionMessage.EXPIRED_MESSAGE.message)
            throw JwtCustomException(JwtExceptionMessage.EXPIRED_MESSAGE)
        } catch (e: UnsupportedJwtException) {
            logger().info(JwtExceptionMessage.UNSUPPORTED_MESSAGE.message)
            throw JwtCustomException(JwtExceptionMessage.UNSUPPORTED_MESSAGE)
        } catch (e: SecurityException) {
            logger().info(JwtExceptionMessage.INVALID_MESSAGE.message)
            throw JwtCustomException(JwtExceptionMessage.INVALID_MESSAGE)
        }
    }
}