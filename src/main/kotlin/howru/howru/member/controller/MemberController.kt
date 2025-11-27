package howru.howru.member.controller

import howru.howru.member.exception.MemberException
import howru.howru.member.exception.MemberExceptionMessage
import howru.howru.logger
import howru.howru.member.controller.constant.MemberApiDocs
import howru.howru.member.controller.constant.MemberRequestHeader
import howru.howru.member.controller.constant.MemberUrl
import howru.howru.member.controller.response.MemberResponse
import howru.howru.member.dto.response.MemberInfo
import howru.howru.member.dto.request.*
import howru.howru.member.log.MemberControllerLog
import howru.howru.member.service.command.MemberCommandService
import howru.howru.member.service.query.MemberQueryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@Tag(name = MemberApiDocs.TAG_NAME)
@RestController
class MemberController
    @Autowired
    constructor(
        private val memberQueryService: MemberQueryService,
        private val memberCommandService: MemberCommandService
    ) {
        @GetMapping(MemberUrl.INFO)
        @Operation(summary = MemberApiDocs.INFO_SUMMARY)
        fun memberInfo(principal: Principal): ResponseEntity<MemberInfo> {
            val member = memberQueryService.getMemberById(id = UUID.fromString(principal.name))
            return ResponseEntity.ok(member)
        }

        @PostMapping(MemberUrl.SIGNUP)
        @Operation(summary = MemberApiDocs.SIGNUP_SUMMARY)
        fun signup(
            @RequestBody @Valid signupRequest: SignupRequest
        ): ResponseEntity<String> {
            memberCommandService.signup(signupRequest)
            logger().info(MemberControllerLog.SIGNUP_SUCCESS + signupRequest.email)

            return MemberResponse.signupSuccess()
        }

        @PostMapping(MemberUrl.LOGIN)
        @Operation(summary = MemberApiDocs.LOGIN_SUMMARY, description = MemberApiDocs.LOGIN_DESCRIPTION)
        fun login(
            @RequestBody @Valid loginRequest: LoginRequest,
            response: HttpServletResponse
        ): ResponseEntity<String> {
            val tokenInfo = memberCommandService.login(loginRequest)
            response.apply {
                addHeader(MemberRequestHeader.ACCESS_TOKEN, tokenInfo.accessToken)
                addHeader(MemberRequestHeader.MEMBER_ID, tokenInfo.id.toString())
            }

            val cookie = ResponseCookie.from("refreshToken", tokenInfo.refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60L * 60 * 24 * 30)
                .sameSite("None")
                .build()

            response.addHeader("Set-Cookie", cookie.toString())
            logger().info(MemberControllerLog.LOGIN_SUCCESS + loginRequest.email)

            return MemberResponse.loginSuccess()
        }

        @PutMapping(MemberUrl.JWT_TOKEN_REISSUE)
        @Operation(summary = MemberApiDocs.JWT_REISSUE_SUMMARY, description = MemberApiDocs.JWT_REISSUE_DESCRIPTION)
        fun jwtTokenReissue(
            @RequestHeader(MemberRequestHeader.ID) id: String?,
            request: HttpServletRequest,
            response: HttpServletResponse
        ): ResponseEntity<String> {
            if (id.isNullOrBlank()) {
                throw MemberException(MemberExceptionMessage.TOKEN_REISSUE_HEADER_IS_NULL, "UNRELIABLE-MEMBER")
            }

            val refreshToken = request.cookies
                ?.firstOrNull { it.name == "refreshToken" }
                ?.value
                ?: throw MemberException(MemberExceptionMessage.TOKEN_REISSUE_HEADER_IS_NULL, "UNRELIABLE-MEMBER")

            val memberId = UUID.fromString(id)
            val reissueJwtToken = memberCommandService.reissueJwtToken(memberId, refreshToken)
            logger().info(MemberControllerLog.JWT_TOKEN_REISSUE_SUCCESS + memberId)
            val refreshCookie = ResponseCookie.from("refreshToken", reissueJwtToken.refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(60L * 60 * 24 * 30)
                .build()
            response.addHeader(MemberRequestHeader.ACCESS_TOKEN, reissueJwtToken.accessToken)
            response.addHeader("Set-Cookie", refreshCookie.toString())

            return ResponseEntity.ok("Reissue Token Success!")
        }

        @PatchMapping(MemberUrl.UPDATE_PASSWORD)
        @Operation(summary = MemberApiDocs.UPDATE_PW_SUMMARY)
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
        @Operation(summary = MemberApiDocs.LOCK_ON_SUMMARY)
        fun lockOn(principal: Principal): ResponseEntity<String> {
            val memberId = UUID.fromString(principal.name)
            memberCommandService.memberLockOn(memberId)
            logger().info(MemberControllerLog.LOCK_ON_SUCCESS + memberId)

            return MemberResponse.lockOnSuccess()
        }

        @PatchMapping(MemberUrl.LOCK_OFF)
        @Operation(summary = MemberApiDocs.LOCK_OFF_SUMMARY)
        fun lockOff(principal: Principal): ResponseEntity<String> {
            val memberId = UUID.fromString(principal.name)
            memberCommandService.memberLockOff(memberId)
            logger().info(MemberControllerLog.LOCK_OFF_SUCCESS + memberId)

            return MemberResponse.lockOffSuccess()
        }

        @PostMapping(MemberUrl.LOGOUT)
        @Operation(summary = MemberApiDocs.LOGOUT_SUMMARY, description = MemberApiDocs.LOGOUT_DESCRIPTION)
        fun logout(principal: Principal, response: HttpServletResponse): ResponseEntity<String> {
            val memberId = UUID.fromString(principal.name)
            memberCommandService.logout(memberId)
            logger().info(MemberControllerLog.LOGOUT_SUCCESS + memberId)
            val deleteCookie = Cookie("refreshToken", "").apply {
                path = "/"
                maxAge = 0
            }
            response.addCookie(deleteCookie)

            return MemberResponse.logOutSuccess()
        }

        @PostMapping(MemberUrl.RECOVERY_MEMBER)
        @Operation(summary = MemberApiDocs.RECOVERY_SUMMARY)
        fun recoveryMember(
            @RequestBody @Valid recoveryRequest: RecoveryRequest
        ): ResponseEntity<String> {
            memberCommandService.recoveryMember(recoveryRequest)
            logger().info(MemberControllerLog.RECOVERY_SUCCESS + recoveryRequest.email)

            return MemberResponse.recoverySuccess()
        }

        @DeleteMapping(MemberUrl.WITHDRAW)
        @Operation(summary = MemberApiDocs.WITHDRAW_SUMMARY)
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
