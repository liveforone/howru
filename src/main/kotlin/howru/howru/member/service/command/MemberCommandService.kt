package howru.howru.member.service.command

import howru.howru.exception.exception.MemberException
import howru.howru.exception.message.MemberExceptionMessage
import howru.howru.globalConfig.cache.constant.CacheName
import howru.howru.jwt.filterLogic.JwtTokenProvider
import howru.howru.globalUtil.isMatchPassword
import howru.howru.jwt.dto.JwtTokenInfo
import howru.howru.jwt.dto.ReissuedTokenInfo
import howru.howru.jwt.service.JwtTokenService
import howru.howru.member.cache.MemberCache
import howru.howru.member.domain.Member
import howru.howru.member.dto.request.LoginRequest
import howru.howru.member.dto.request.SignupRequest
import howru.howru.member.dto.request.WithdrawRequest
import howru.howru.member.dto.request.UpdateEmail
import howru.howru.member.dto.request.UpdatePassword
import howru.howru.member.repository.MemberQuery
import howru.howru.member.repository.MemberRepository
import howru.howru.member.service.validator.MemberServiceValidator
import howru.howru.reportState.service.command.ReportStateCommandService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
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
    private val memberQuery: MemberQuery,
    private val reportStateCommandService: ReportStateCommandService,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberServiceValidator: MemberServiceValidator,
    private val jwtTokenService: JwtTokenService
) {

    fun signupMember(signupRequest: SignupRequest) {
        with(signupRequest) {
            memberServiceValidator.validateDuplicateEmail(email!!)
            Member.create(email, pw!!, nickName!!).also {
                memberRepository.save(it)
                reportStateCommandService.createRepostState(it)
            }
        }
    }

    fun login(loginRequest: LoginRequest): JwtTokenInfo {
        val authentication: Authentication = authenticationManagerBuilder
            .`object`
            .authenticate(UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.pw))

        return jwtTokenProvider.generateToken(authentication).also {
            jwtTokenService.createRefreshToken(it.uuid, it.refreshToken)
        }
    }

    fun reissueJwtToken(uuid: UUID, refreshToken: String): ReissuedTokenInfo {
        val auth = memberQuery.findAuthByUUID(uuid)
        return jwtTokenService.reissueToken(uuid, refreshToken, auth)
    }

    @CacheEvict(cacheNames = [CacheName.MEMBER], key = MemberCache.KEY)
    fun updateEmail(updateEmail: UpdateEmail, uuid: UUID) {
        memberServiceValidator.validateDuplicateEmail(updateEmail.newEmail!!)
        memberQuery.findOneByUUID(uuid)
            .also { it.updateEmail(updateEmail.newEmail) }
    }

    fun updatePassword(updatePassword: UpdatePassword, uuid: UUID) {
        with(updatePassword) {
            memberQuery.findOneByUUID(uuid)
                .also { it.updatePw(newPassword!!, oldPassword!!) }
        }
    }

    @CacheEvict(cacheNames = [CacheName.MEMBER], key = MemberCache.KEY)
    fun memberLockOn(uuid: UUID) {
        memberQuery.findOneByUUID(uuid)
            .also { it.lockOn() }
    }

    @CacheEvict(cacheNames = [CacheName.MEMBER], key = MemberCache.KEY)
    fun memberLockOff(uuid: UUID) {
        memberQuery.findOneByUUID(uuid)
            .also { it.lockOff() }
    }

    @CacheEvict(cacheNames = [CacheName.MEMBER], key = MemberCache.KEY)
    fun logout(uuid: UUID) {
        jwtTokenService.clearRefreshToken(uuid)
    }

    @CacheEvict(cacheNames = [CacheName.MEMBER], key = MemberCache.KEY)
    fun withdraw(withdrawRequest: WithdrawRequest, uuid: UUID) {
        memberQuery.findOneByUUID(uuid)
            .takeIf { isMatchPassword(withdrawRequest.pw!!, it.pw) }
            ?.also {
                memberRepository.delete(it)
                jwtTokenService.removeRefreshToken(uuid)
            } ?: throw MemberException(MemberExceptionMessage.WRONG_PASSWORD, uuid.toString())
    }
}