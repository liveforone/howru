package howru.howru.comments.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CommentsControllerAdvice {
    @ExceptionHandler(CommentsException::class)
    fun handleCommentsException(commentsException: CommentsException): ResponseEntity<String> =
        ResponseEntity
            .status(commentsException.commentsExceptionMessage.status)
            .body(commentsException.message + commentsException.commentId)
}
