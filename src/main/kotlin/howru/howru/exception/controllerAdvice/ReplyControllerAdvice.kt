package howru.howru.exception.controllerAdvice

import howru.howru.exception.exception.ReplyException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ReplyControllerAdvice {
    @ExceptionHandler(ReplyException::class)
    fun replyExceptionHandle(replyException: ReplyException): ResponseEntity<String> {
        return ResponseEntity
            .status(replyException.replyExceptionMessage.status)
            .body(replyException.message + replyException.replyId)
    }
}