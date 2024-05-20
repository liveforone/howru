package howru.howru.post.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class PostControllerAdvice {
    @ExceptionHandler(PostException::class)
    fun handlePostException(postException: PostException): ResponseEntity<String> {
        return ResponseEntity
            .status(postException.postExceptionMessage.status)
            .body(postException.message + postException.postId)
    }
}
