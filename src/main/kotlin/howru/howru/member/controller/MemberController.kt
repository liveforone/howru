package howru.howru.member.controller

import howru.howru.exception.exception.MemberException
import howru.howru.exception.message.MemberExceptionMessage
import howru.howru.globalUtil.validateBinding
import howru.howru.logger
import howru.howru.member.controller.constant.MemberControllerConstant
import howru.howru.member.log.MemberControllerLog
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
        logger().info(MemberControllerLog.SIGNUP_SUCCESS + signupRequest.email)

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
        logger().info(MemberControllerLog.LOGIN_SUCCESS + loginRequest.email)

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

        val memberUUID = UUID.fromString(uuid)
        val reissueJwtToken = memberCommandService.reissueJwtToken(memberUUID, refreshToken)
        logger().info(MemberControllerLog.JWT_TOKEN_REISSUE + memberUUID)
        return ResponseEntity.ok(reissueJwtToken)
    }

    @PatchMapping(MemberUrl.UPDATE_EMAIL)
    fun updateEmail(
        @RequestBody @Valid updateEmail: UpdateEmail,
        bindingResult: BindingResult,
        principal: Principal
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        val memberUUID = UUID.fromString(principal.name)
        memberCommandService.updateEmail(updateEmail, memberUUID)
        logger().info(MemberControllerLog.UPDATE_EMAIL_SUCCESS + memberUUID)

        return MemberResponse.updateEmailSuccess()
    }

    @PatchMapping(MemberUrl.UPDATE_PASSWORD)
    fun updatePassword(
        @RequestBody @Valid updatePassword: UpdatePassword,
        bindingResult: BindingResult,
        principal: Principal
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        val memberUUID = UUID.fromString(principal.name)
        memberCommandService.updatePassword(updatePassword, memberUUID)
        logger().info(MemberControllerLog.UPDATE_PW_SUCCESS + memberUUID)

        return MemberResponse.updatePwSuccess()
    }

    @PatchMapping(MemberUrl.LOCK_ON)
    fun lockOn(principal: Principal): ResponseEntity<*> {
        val memberUUID = UUID.fromString(principal.name)
        memberCommandService.memberLockOn(memberUUID)
        logger().info(MemberControllerLog.LOCK_ON_SUCCESS + memberUUID)

        return MemberResponse.lockOnSuccess()
    }

    @PatchMapping(MemberUrl.LOCK_OFF)
    fun lockOff(principal: Principal): ResponseEntity<*> {
        val memberUUID = UUID.fromString(principal.name)
        memberCommandService.memberLockOff(memberUUID)
        logger().info(MemberControllerLog.LOCK_OFF_SUCCESS + memberUUID)

        return MemberResponse.lockOffSuccess()
    }

    @PostMapping(MemberUrl.LOGOUT)
    fun logout(principal: Principal): ResponseEntity<*> {
        val memberUUID = UUID.fromString(principal.name)
        memberCommandService.logout(memberUUID)
        logger().info(MemberControllerLog.LOGOUT_SUCCESS + memberUUID)
        return MemberResponse.logOutSuccess()
    }

    @DeleteMapping(MemberUrl.WITHDRAW)
    fun withdraw(
        @RequestBody @Valid withdrawRequest: WithdrawRequest,
        bindingResult: BindingResult,
        principal: Principal
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        val memberUUID = UUID.fromString(principal.name)
        memberCommandService.withdraw(withdrawRequest, memberUUID)
        logger().info(MemberControllerLog.WITHDRAW_SUCCESS + memberUUID)

        return MemberResponse.withdrawSuccess()
    }
}