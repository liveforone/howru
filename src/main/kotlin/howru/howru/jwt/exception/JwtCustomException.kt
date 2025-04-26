package howru.howru.jwt.exception

class JwtCustomException(
    val jwtExceptionMessage: JwtExceptionMessage
) : RuntimeException(jwtExceptionMessage.message)
