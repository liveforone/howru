package howru.howru.member.controller

import howru.howru.member.exception.MemberException
import howru.howru.member.exception.MemberExceptionMessage
import howru.howru.jwt.dto.JwtTokenInfo
import howru.howru.logger
import howru.howru.member.controller.constant.MemberRequestHeader
import howru.howru.member.controller.constant.MemberUrl
import howru.howru.member.controller.response.MemberResponse
import howru.howru.member.dto.response.MemberInfo
import howru.howru.member.dto.request.*
import howru.howru.member.log.MemberControllerLog
import howru.howru.member.service.command.MemberCommandService
import howru.howru.member.service.query.MemberQueryService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
class MemberController
    @Autowired
    constructor(
        private val memberQueryService: MemberQueryService,
        private val memberCommandService: MemberCommandService
    ) {
        @GetMapping(MemberUrl.INFO)
        fun getMemberInfo(principal: Principal): ResponseEntity<MemberInfo> {
            val member = memberQueryService.getMemberById(id = UUID.fromString(principal.name))
            return ResponseEntity.ok(member)
        }

        @PostMapping(MemberUrl.SIGNUP)
        fun signup(
            @RequestBody @Valid signupRequest: SignupRequest
        ): ResponseEntity<String> {
            memberCommandService.signup(signupRequest)
            logger().info(MemberControllerLog.SIGNUP_SUCCESS + signupRequest.email)

            return MemberResponse.signupSuccess()
        }

        @PostMapping(MemberUrl.LOGIN)
        fun login(
            @RequestBody @Valid loginRequest: LoginRequest,
            response: HttpServletResponse
        ): ResponseEntity<String> {
            val tokenInfo = memberCommandService.login(loginRequest)
            response.apply {
                addHeader(MemberRequestHeader.ACCESS_TOKEN, tokenInfo.accessToken)
                addHeader(MemberRequestHeader.REFRESH_TOKEN, tokenInfo.refreshToken)
                addHeader(MemberRequestHeader.MEMBER_ID, tokenInfo.id.toString())
            }
            logger().info(MemberControllerLog.LOGIN_SUCCESS + loginRequest.email)

            return MemberResponse.loginSuccess()
        }

        @PutMapping(MemberUrl.JWT_TOKEN_REISSUE)
        fun jwtTokenReissue(
            @RequestHeader(MemberRequestHeader.ID) id: String?,
            @RequestHeader(MemberRequestHeader.REFRESH_TOKEN) refreshToken: String?
        ): ResponseEntity<JwtTokenInfo> {
            if (id.isNullOrBlank() || refreshToken.isNullOrBlank()) {
                throw MemberException(MemberExceptionMessage.TOKEN_REISSUE_HEADER_IS_NULL, "UNRELIABLE-MEMBER")
            }

            val memberId = UUID.fromString(id)
            val reissueJwtToken = memberCommandService.reissueJwtToken(memberId, refreshToken)
            logger().info(MemberControllerLog.JWT_TOKEN_REISSUE_SUCCESS + memberId)

            return ResponseEntity.ok(reissueJwtToken)
        }

        @PatchMapping(MemberUrl.UPDATE_PASSWORD)
        fun updatePassword(
            @RequestBody @Valid updatePassword: UpdatePassword,
            principal: Principal
        ): ResponseEntity<String> {
            val memberId = UUID.fromString(principal.name)
            memberCommandService.updatePassword(updatePassword, memberId)
            logger().info(MemberControllerLog.UPDATE_PW_SUCCESS + memberId)

            return MemberResponse.updatePwSuccess()
        }

        @PatchMapping(MemberUrl.LOCK_ON)
        fun lockOn(principal: Principal): ResponseEntity<String> {
            val memberId = UUID.fromString(principal.name)
            memberCommandService.memberLockOn(memberId)
            logger().info(MemberControllerLog.LOCK_ON_SUCCESS + memberId)

            return MemberResponse.lockOnSuccess()
        }

        @PatchMapping(MemberUrl.LOCK_OFF)
        fun lockOff(principal: Principal): ResponseEntity<String> {
            val memberId = UUID.fromString(principal.name)
            memberCommandService.memberLockOff(memberId)
            logger().info(MemberControllerLog.LOCK_OFF_SUCCESS + memberId)

            return MemberResponse.lockOffSuccess()
        }

        @PostMapping(MemberUrl.LOGOUT)
        fun logout(principal: Principal): ResponseEntity<String> {
            val memberId = UUID.fromString(principal.name)
            memberCommandService.logout(memberId)
            logger().info(MemberControllerLog.LOGOUT_SUCCESS + memberId)

            return MemberResponse.logOutSuccess()
        }

        @PostMapping(MemberUrl.RECOVERY_MEMBER)
        fun recoveryMember(
            @RequestBody @Valid recoveryRequest: RecoveryRequest
        ): ResponseEntity<String> {
            memberCommandService.recoveryMember(recoveryRequest)
            logger().info(MemberControllerLog.RECOVERY_SUCCESS + recoveryRequest.email)

            return MemberResponse.recoverySuccess()
        }

        @DeleteMapping(MemberUrl.WITHDRAW)
        fun withdraw(
            @RequestBody @Valid withdrawRequest: WithdrawRequest,
            principal: Principal
        ): ResponseEntity<String> {
            val memberId = UUID.fromString(principal.name)
            memberCommandService.withdraw(withdrawRequest, memberId)
            logger().info(MemberControllerLog.WITHDRAW_SUCCESS + memberId)

            return MemberResponse.withdrawSuccess()
        }
    }
