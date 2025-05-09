package howru.howru.subscribe.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SubscribeControllerAdvice {
    @ExceptionHandler(SubscribeException::class)
    fun handleSubscribeException(subscribeException: SubscribeException): ResponseEntity<String> =
        ResponseEntity
            .status(subscribeException.subscribeExceptionMessage.status)
            .body(subscribeException.message + subscribeException.followerUUID)
}
