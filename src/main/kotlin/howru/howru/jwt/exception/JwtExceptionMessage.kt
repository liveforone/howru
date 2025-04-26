package howru.howru.jwt.exception

enum class JwtExceptionMessage(
    val status: Int,
    val message: String
) {
    TOKEN_IS_NULL(404, "Token Is Null"),
    INVALID_TOKEN(401, "Invalid JWT Token"),
    EXPIRED_JWT_TOKEN(401, "Expired Jwt Token"),
    UNSUPPORTED_TOKEN(401, "Unsupported JWT Token"),
    EMPTY_CLAIMS(404, "JWT claims string is empty."),
    NOT_EXIST_REFRESH_TOKEN(404, "Refresh Token is not exist"),
    UN_MATCH_REFRESH_TOKEN(401, "Un Match Refresh Token")
}
