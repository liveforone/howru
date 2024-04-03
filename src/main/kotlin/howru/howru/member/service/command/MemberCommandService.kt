package howru.howru.member.service.command

import howru.howru.exception.exception.MemberException
import howru.howru.exception.message.MemberExceptionMessage
import howru.howru.globalUtil.isMatchPassword
import howru.howru.jwt.dto.JwtTokenInfo
import howru.howru.jwt.filterLogic.JwtTokenProvider
import howru.howru.jwt.service.JwtTokenService
import howru.howru.logger
import howru.howru.member.domain.Member
import howru.howru.member.dto.request.*
import howru.howru.member.log.MemberServiceLog
import howru.howru.member.repository.MemberRepository
import howru.howru.reportState.service.command.ReportStateCommandService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class MemberCommandService
    @Autowired
    constructor(
        private val memberRepository: MemberRepository,
        private val reportStateCommandService: ReportStateCommandService,
        private val authenticationManagerBuilder: AuthenticationManagerBuilder,
        private val jwtTokenProvider: JwtTokenProvider,
        private val jwtTokenService: JwtTokenService
    ) {
        fun signup(signupRequest: SignupRequest) {
            with(signupRequest) {
                Member.create(email!!, pw!!, nickName!!).also {
                    memberRepository.save(it)
                    reportStateCommandService.createRepostState(it)
                }
            }
        }

        fun login(loginRequest: LoginRequest): JwtTokenInfo {
            val authentication: Authentication =
                authenticationManagerBuilder
                    .`object`
                    .authenticate(UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.pw))

            return jwtTokenProvider.generateToken(authentication).also {
                jwtTokenService.createRefreshToken(it.id, it.refreshToken)
            }
        }

        fun reissueJwtToken(
            id: UUID,
            refreshToken: String
        ): JwtTokenInfo {
            val auth = memberRepository.findAuthById(id)
            return jwtTokenService.reissueToken(id, refreshToken, auth)
        }

        fun updatePassword(
            updatePassword: UpdatePassword,
            id: UUID
        ) {
            with(updatePassword) {
                memberRepository.findMemberById(id).also { it.updatePw(newPassword!!, oldPassword!!) }
            }
        }

        fun memberLockOn(id: UUID) {
            memberRepository.findMemberById(id).also { it.lockOn() }
        }

        fun memberLockOff(id: UUID) {
            memberRepository.findMemberById(id).also { it.lockOff() }
        }

        fun logout(id: UUID) {
            jwtTokenService.removeRefreshToken(id)
        }

        fun recoveryMember(recoveryRequest: RecoveryRequest) {
            with(recoveryRequest) {
                memberRepository.findMemberByEmailIncludeWithdraw(email!!).also { it.recovery(pw!!) }
            }
        }

        fun withdraw(
            withdrawRequest: WithdrawRequest,
            id: UUID
        ) {
            memberRepository.findMemberById(id)
                .takeIf { isMatchPassword(withdrawRequest.pw!!, it.pw) }
                ?.also {
                    it.withdraw()
                    jwtTokenService.removeRefreshToken(id)
                }
                ?: run {
                    logger().warn(MemberServiceLog.WRONG_PW + id)
                    throw MemberException(MemberExceptionMessage.WRONG_PASSWORD, id.toString())
                }
        }
    }
