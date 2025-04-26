package howru.howru.global.exception

import jakarta.validation.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.*

object GlobalAdviceConstant {
    const val DUPLICATE_ENTITY_VAL = "데이터 베이스 무결성 조건을 위반하였습니다."
}

@RestControllerAdvice
class GlobalControllerAdvice {
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDuplicateEntityValueException(): ResponseEntity<String> =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(GlobalAdviceConstant.DUPLICATE_ENTITY_VAL)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(exception: MethodArgumentNotValidException): ResponseEntity<String> {
        val bindingResult = exception.bindingResult
        val errorMessage =
            Objects
                .requireNonNull(bindingResult.fieldError)
                ?.defaultMessage
        return ResponseEntity.badRequest().body(errorMessage)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(ex: ConstraintViolationException): ResponseEntity<*> =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ex.message)
}
