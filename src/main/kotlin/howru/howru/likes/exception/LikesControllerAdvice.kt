package howru.howru.likes.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class LikesControllerAdvice {
    @ExceptionHandler(LikesException::class)
    fun handleLikesException(likesException: LikesException): ResponseEntity<String> =
        ResponseEntity
            .status(likesException.likesExceptionMessage.status)
            .body(likesException.message + likesException.postId)
}
