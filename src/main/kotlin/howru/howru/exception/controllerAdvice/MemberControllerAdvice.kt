package howru.howru.exception.controllerAdvice

import howru.howru.exception.controllerAdvice.constant.MemberAdviceConstant
import howru.howru.exception.exception.JwtCustomException
import howru.howru.exception.exception.MemberException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class MemberControllerAdvice {

    @ExceptionHandler(BadCredentialsException::class)
    fun loginFailHandle(): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(MemberAdviceConstant.LOGIN_FAIL)
    }

    @ExceptionHandler(MemberException::class)
    fun memberExceptionHandle(memberException: MemberException): ResponseEntity<String> {
        return ResponseEntity
            .status(memberException.memberExceptionMessage.status)
            .body(memberException.message + memberException.memberIdentifier)
    }

    @ExceptionHandler(JwtCustomException::class)
    fun jwtCustomExceptionHandle(jwtCustomException: JwtCustomException): ResponseEntity<String> {
        return ResponseEntity
            .status(jwtCustomException.jwtExceptionMessage.status)
            .body(jwtCustomException.message)
    }
}