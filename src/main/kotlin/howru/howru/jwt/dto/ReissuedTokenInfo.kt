package howru.howru.jwt.dto

data class ReissuedTokenInfo(val accessToken: String, val refreshToken: String) {
    companion object {
        fun create(accessToken: String, refreshToken: String): ReissuedTokenInfo {
            return ReissuedTokenInfo(accessToken, refreshToken)
        }
    }
}