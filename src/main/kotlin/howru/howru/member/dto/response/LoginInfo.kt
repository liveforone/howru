package howru.howru.member.dto.response

class LoginInfo private constructor(
    val uuid: String,
    val accessToken: String,
    val refreshToken: String
) {
    companion object {
        fun create(uuid: String, accessToken:String, refreshToken:String): LoginInfo {
            return LoginInfo(uuid, accessToken, refreshToken)
        }
    }
}