package howru.howru.exception.controllerAdvice

import howru.howru.exception.controllerAdvice.constant.GlobalAdviceConstant
import jakarta.validation.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.*

@RestControllerAdvice
class GlobalControllerAdvice {
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDuplicateEntityValueException(): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(GlobalAdviceConstant.DUPLICATE_ENTITY_VAL)
    }

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
    fun handleConstraintViolationException(ex: ConstraintViolationException): ResponseEntity<*> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ex.message)
    }
}
