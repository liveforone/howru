package howru.howru.reportState.exceptioin

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ReportStateControllerAdvice {
    @ExceptionHandler(ReportStateException::class)
    fun handleReportStateException(reportStateException: ReportStateException): ResponseEntity<String> {
        return ResponseEntity
            .status(reportStateException.repostStateExceptionMessage.status)
            .body(reportStateException.message + reportStateException.memberIdentifier)
    }
}
