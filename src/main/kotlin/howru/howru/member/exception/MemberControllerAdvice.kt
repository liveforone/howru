package howru.howru.member.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

object MemberAdviceConstant {
    const val LOGIN_FAIL = "로그인에 실패했습니다."
}

@RestControllerAdvice
class MemberControllerAdvice {
    @ExceptionHandler(BadCredentialsException::class)
    fun handleLoginFail(): ResponseEntity<String> =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(MemberAdviceConstant.LOGIN_FAIL)

    @ExceptionHandler(MemberException::class)
    fun handleMemberException(memberException: MemberException): ResponseEntity<String> =
        ResponseEntity
            .status(memberException.memberExceptionMessage.status)
            .body(memberException.message + memberException.memberIdentifier)
}
