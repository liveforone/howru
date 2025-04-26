package howru.howru.jwt.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class JwtControllerAdvice {
    @ExceptionHandler(JwtCustomException::class)
    fun handleJwtCustomException(jwtCustomException: JwtCustomException): ResponseEntity<String> =
        ResponseEntity
            .status(jwtCustomException.jwtExceptionMessage.status)
            .body(jwtCustomException.message)
}
