package howru.howru.jwt.filterLogic

import howru.howru.exception.exception.JwtCustomException
import howru.howru.exception.message.JwtExceptionMessage
import howru.howru.jwt.constant.JwtConstant
import howru.howru.logger
import howru.howru.jwt.dto.JwtTokenInfo
import howru.howru.jwt.dto.ReissuedTokenInfo
import howru.howru.member.domain.Role
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
class JwtTokenProvider (@Value(JwtConstant.SECRET_KEY_PATH) private var secretKey: String) {
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

    fun reissueToken(uuid: UUID, role: Role): ReissuedTokenInfo {
        return ReissuedTokenInfo.create(generateAccessTokenWhenReissue(uuid, role), generateRefreshToken())
    }

    private fun generateAccessTokenWhenReissue(uuid: UUID, role: Role): String {
        return Jwts.builder()
            .setSubject(uuid.toString())
            .claim(JwtConstant.CLAIM_NAME, role.auth)
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

    fun validateToken(token: String) {
        if (token.isBlank()) {
            throw JwtCustomException(JwtExceptionMessage.TOKEN_IS_NULL)
        }
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
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