package howru.howru.exception.exception

import howru.howru.exception.message.JwtExceptionMessage

class JwtCustomException(val jwtExceptionMessage: JwtExceptionMessage) : RuntimeException(jwtExceptionMessage.message)
