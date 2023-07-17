package howru.howru.member.service.command

import howru.howru.exception.exception.MemberException
import howru.howru.exception.message.MemberExceptionMessage
import howru.howru.jwt.JwtTokenProvider
import howru.howru.jwt.TokenInfo
import howru.howru.member.domain.Member
import howru.howru.member.domain.Role
import howru.howru.member.domain.util.PasswordUtil
import howru.howru.member.dto.request.LoginRequest
import howru.howru.member.dto.request.SignupRequest
import howru.howru.member.dto.request.WithdrawRequest
import howru.howru.member.dto.update.UpdateEmail
import howru.howru.member.dto.update.UpdatePassword
import howru.howru.member.repository.MemberRepository
import howru.howru.member.service.validator.MemberServiceValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class MemberCommandService @Autowired constructor(
    private val memberRepository: MemberRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberServiceValidator: MemberServiceValidator
) {

    fun signupMember(signupRequest: SignupRequest): UUID {
        return with(signupRequest) {
            memberServiceValidator.validateDuplicateEmail(email!!)
            Member.create(email, pw!!, auth = Role.MEMBER)
                .run { memberRepository.save(this).uuid }
        }
    }

    fun login(loginRequest: LoginRequest): TokenInfo {
        val authentication: Authentication = authenticationManagerBuilder
            .`object`
            .authenticate(UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.pw))

        return jwtTokenProvider.generateToken(authentication)
    }

    fun updateEmail(updateEmail: UpdateEmail, uuid: UUID) {
        memberServiceValidator.validateDuplicateEmail(updateEmail.newEmail!!)
        memberRepository.findOneByUUID(uuid)
            .also { it.updateEmail(updateEmail.newEmail) }
    }

    fun updatePassword(updatePassword: UpdatePassword, uuid: UUID) {
        with(updatePassword) {
            memberRepository.findOneByUUID(uuid)
                .also { it.updatePw(newPassword!!, oldPassword!!) }
        }
    }

    fun addReportCount(uuid: UUID) {
        memberRepository.findOneByUUID(uuid)
            .also { it.addReport() }
    }

    fun withdraw(withdrawRequest: WithdrawRequest, uuid: UUID) {
        memberRepository.findOneByUUID(uuid)
            .takeIf { PasswordUtil.isMatchPassword(withdrawRequest.pw!!, it.pw) }
            ?.also { memberRepository.delete(it) }
            ?: throw MemberException(MemberExceptionMessage.WRONG_PASSWORD)
    }
}