package howru.howru.jwt.dto

import java.util.UUID

data class JwtTokenInfo (
    val uuid: UUID,
    val accessToken: String,
    val refreshToken: String
) {
    companion object {
        fun create(uuid: UUID, accessToken:String, refreshToken:String): JwtTokenInfo {
            return JwtTokenInfo(uuid, accessToken, refreshToken)
        }
    }
}