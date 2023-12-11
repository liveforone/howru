package howru.howru.exception.message

enum class JwtExceptionMessage(val status: Int, val message: String) {
    TOKEN_IS_NULL(404, "Token Is Null"),
    INVALID_MESSAGE(401, "Invalid JWT Token"),
    EXPIRED_MESSAGE(401, "Expired JWT Token"),
    UNSUPPORTED_MESSAGE(401, "Unsupported JWT Token"),
    EMPTY_CLAIMS(404, "JWT claims string is empty."),
    NOT_EXIST_REFRESH_TOKEN(404, "Refresh Token is not exist")
}