package howru.howru.jwt.constant

object JwtConstant {
    const val ACCESS_TOKEN = "access-token"
    const val REFRESH_TOKEN = "refresh-token"
    const val HEADER = "Authorization"
    const val CLAIM_NAME = "auth"
    const val TWO_HOUR_MS = 7200000
    const val BEARER_TOKEN = "Bearer"
    const val SECRET_KEY_PATH = "\${jwt.secret}"
    const val TOKEN_SUB_INDEX = 7
}