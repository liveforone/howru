package howru.howru.member.controller

import howru.howru.exception.exception.MemberException
import howru.howru.exception.message.MemberExceptionMessage
import howru.howru.globalUtil.validateBinding
import howru.howru.logger
import howru.howru.member.controller.constant.MemberControllerConstant
import howru.howru.member.controller.constant.MemberControllerLog
import howru.howru.member.controller.constant.MemberHeader
import howru.howru.member.controller.constant.MemberUrl
import howru.howru.member.controller.response.MemberResponse
import howru.howru.member.dto.request.LoginRequest
import howru.howru.member.dto.request.SignupRequest
import howru.howru.member.dto.request.WithdrawRequest
import howru.howru.member.dto.request.UpdateEmail
import howru.howru.member.dto.request.UpdatePassword
import howru.howru.member.service.command.MemberCommandService
import howru.howru.member.service.query.MemberQueryService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
class MemberController @Autowired constructor(
    private val memberQueryService: MemberQueryService,
    private val memberCommandService: MemberCommandService
) {

    @GetMapping(MemberUrl.INFO)
    fun memberInfo(principal: Principal): ResponseEntity<*> {
        val member = memberQueryService.getMemberByUUID(uuid = UUID.fromString(principal.name))
        return MemberResponse.infoSuccess(member)
    }

    @PostMapping(MemberUrl.SIGNUP_MEMBER)
    fun signupMember(
        @RequestBody @Valid signupRequest: SignupRequest,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        memberCommandService.signupMember(signupRequest)
        logger().info(MemberControllerLog.SIGNUP_SUCCESS.log)

        return MemberResponse.signupSuccess()
    }

    @PostMapping(MemberUrl.LOGIN)
    fun login(
        @RequestBody @Valid loginRequest: LoginRequest,
        bindingResult: BindingResult,
        response: HttpServletResponse
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        val tokenInfo = memberCommandService.login(loginRequest)
        response.apply {
            addHeader(MemberControllerConstant.ACCESS_TOKEN, tokenInfo.accessToken)
            addHeader(MemberControllerConstant.REFRESH_TOKEN, tokenInfo.refreshToken)
            addHeader(MemberControllerConstant.MEMBER_UUID, tokenInfo.uuid.toString())
        }
        logger().info(MemberControllerLog.LOGIN_SUCCESS.log)

        return MemberResponse.loginSuccess()
    }

    @PutMapping(MemberUrl.JWT_TOKEN_REISSUE)
    fun jwtTokenReissue(
        @RequestHeader(MemberHeader.UUID) uuid: String?,
        @RequestHeader(MemberHeader.REFRESH_TOKEN) refreshToken: String?
    ): ResponseEntity<*> {
        if (uuid.isNullOrBlank() || refreshToken.isNullOrBlank()) {
            throw MemberException(MemberExceptionMessage.TOKEN_REISSUE_HEADER_IS_NULL, "UNRELIABLE-MEMBER")
        }

        val reissueJwtToken = memberCommandService.reissueJwtToken(UUID.fromString(uuid), refreshToken)
        logger().info(MemberControllerLog.JWT_TOKEN_REISSUE.log)
        return ResponseEntity.ok(reissueJwtToken)
    }

    @PutMapping(MemberUrl.UPDATE_EMAIL)
    fun updateEmail(
        @RequestBody @Valid updateEmail: UpdateEmail,
        bindingResult: BindingResult,
        principal: Principal
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        memberCommandService.updateEmail(updateEmail, UUID.fromString(principal.name))
        logger().info(MemberControllerLog.UPDATE_EMAIL_SUCCESS.log)

        return MemberResponse.updateEmailSuccess()
    }

    @PutMapping(MemberUrl.UPDATE_PASSWORD)
    fun updatePassword(
        @RequestBody @Valid updatePassword: UpdatePassword,
        bindingResult: BindingResult,
        principal: Principal
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        memberCommandService.updatePassword(updatePassword, UUID.fromString(principal.name))
        logger().info(MemberControllerLog.UPDATE_PW_SUCCESS.log)

        return MemberResponse.updatePwSuccess()
    }

    @PutMapping(MemberUrl.LOCK_ON)
    fun lockOn(principal: Principal): ResponseEntity<*> {
        memberCommandService.memberLockOn(UUID.fromString(principal.name))
        logger().info(MemberControllerLog.LOCK_ON_SUCCESS.log)

        return MemberResponse.lockOnSuccess()
    }

    @PutMapping(MemberUrl.LOCK_OFF)
    fun lockOff(principal: Principal): ResponseEntity<*> {
        memberCommandService.memberLockOff(UUID.fromString(principal.name))
        logger().info(MemberControllerLog.LOCK_OFF_SUCCESS.log)

        return MemberResponse.lockOffSuccess()
    }

    @PostMapping(MemberUrl.LOGOUT)
    fun logout(principal: Principal): ResponseEntity<*> {
        memberCommandService.logout(UUID.fromString(principal.name))
        logger().info(MemberControllerLog.LOGOUT_SUCCESS.log)
        return MemberResponse.logOutSuccess()
    }

    @DeleteMapping(MemberUrl.WITHDRAW)
    fun withdraw(
        @RequestBody @Valid withdrawRequest: WithdrawRequest,
        bindingResult: BindingResult,
        principal: Principal
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        memberCommandService.withdraw(withdrawRequest, UUID.fromString(principal.name))
        logger().info(MemberControllerLog.WITHDRAW_SUCCESS.log)

        return MemberResponse.withdrawSuccess()
    }
}