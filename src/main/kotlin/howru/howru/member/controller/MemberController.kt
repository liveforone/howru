package howru.howru.member.controller

import howru.howru.globalUtil.validateBinding
import howru.howru.jwt.constant.JwtConstant
import howru.howru.logger
import howru.howru.member.controller.constant.MemberControllerLog
import howru.howru.member.controller.constant.MemberParam
import howru.howru.member.controller.constant.MemberUrl
import howru.howru.member.controller.response.MemberResponse
import howru.howru.member.dto.request.LoginRequest
import howru.howru.member.dto.request.SignupRequest
import howru.howru.member.dto.request.WithdrawRequest
import howru.howru.member.dto.update.UpdateEmail
import howru.howru.member.dto.update.UpdatePassword
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
            addHeader(JwtConstant.ACCESS_TOKEN, tokenInfo.accessToken)
            addHeader(JwtConstant.REFRESH_TOKEN, tokenInfo.refreshToken)
        }
        logger().info(MemberControllerLog.LOGIN_SUCCESS.log)

        return MemberResponse.loginSuccess()
    }

    @PutMapping(MemberUrl.UPDATE_EMAIL)
    fun updateEmail(
        @PathVariable(MemberParam.UUID) uuid: UUID,
        @RequestBody @Valid updateEmail: UpdateEmail,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        memberCommandService.updateEmail(updateEmail, uuid)
        logger().info(MemberControllerLog.UPDATE_EMAIL_SUCCESS.log)

        return MemberResponse.updateEmailSuccess()
    }

    @PutMapping(MemberUrl.UPDATE_PASSWORD)
    fun updatePassword(
        @PathVariable(MemberParam.UUID) uuid: UUID,
        @RequestBody @Valid updatePassword: UpdatePassword,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        memberCommandService.updatePassword(updatePassword, uuid)
        logger().info(MemberControllerLog.UPDATE_PW_SUCCESS.log)

        return MemberResponse.updatePwSuccess()
    }

    @PutMapping(MemberUrl.LOCK_ON)
    fun lockOn(@PathVariable(MemberParam.UUID) uuid: UUID): ResponseEntity<*> {
        memberCommandService.memberLockOn(uuid)
        logger().info(MemberControllerLog.LOCK_ON_SUCCESS.log)

        return MemberResponse.lockOnSuccess()
    }

    @PutMapping(MemberUrl.LOCK_OFF)
    fun lockOff(@PathVariable(MemberParam.UUID) uuid: UUID): ResponseEntity<*> {
        memberCommandService.memberLockOff(uuid)
        logger().info(MemberControllerLog.LOCK_OFF_SUCCESS.log)

        return MemberResponse.lockOffSuccess()
    }

    @DeleteMapping(MemberUrl.WITHDRAW)
    fun withdraw(
        @PathVariable(MemberParam.UUID) uuid: UUID,
        @RequestBody @Valid withdrawRequest: WithdrawRequest,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        memberCommandService.withdraw(withdrawRequest, uuid)
        logger().info(MemberControllerLog.WITHDRAW_SUCCESS.log)

        return MemberResponse.withdrawSuccess()
    }
}