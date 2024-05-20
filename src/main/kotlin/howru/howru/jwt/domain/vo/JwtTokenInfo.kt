package howru.howru.jwt.domain.vo

import java.util.UUID

data class JwtTokenInfo(
    val id: UUID,
    val accessToken: String,
    val refreshToken: String
) {
    companion object {
        fun create(
            id: UUID,
            accessToken: String,
            refreshToken: String
        ) = JwtTokenInfo(id, accessToken, refreshToken)
    }
}
