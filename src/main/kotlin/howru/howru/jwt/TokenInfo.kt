package howru.howru.jwt

class TokenInfo private constructor(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String
) {
    companion object {
        fun create(grantType:String, accessToken:String, refreshToken:String): TokenInfo {
            return TokenInfo(grantType, accessToken, refreshToken)
        }
    }
}