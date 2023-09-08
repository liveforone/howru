package howru.howru.exception.controllerAdvice

import howru.howru.exception.exception.ReportStateException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ReportStateControllerAdvice {
    @ExceptionHandler(ReportStateException::class)
    fun reportStateExceptionHandle(reportStateException: ReportStateException): ResponseEntity<String> {
        return ResponseEntity
            .status(reportStateException.repostStateExceptionMessage.status)
            .body(reportStateException.message + reportStateException.memberIdentifier)
    }
}