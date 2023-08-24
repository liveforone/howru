package howru.howru.reportState.service.command

import howru.howru.member.dto.request.LoginRequest
import howru.howru.member.dto.request.SignupRequest
import howru.howru.member.service.command.MemberCommandService
import howru.howru.reportState.domain.MemberState
import howru.howru.reportState.dto.request.ReportMember
import howru.howru.reportState.service.query.ReportStateQueryService
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class RepostStateCommandServiceTest @Autowired constructor(
    private val entityManager: EntityManager,
    private val memberCommandService: MemberCommandService,
    private val repostStateCommandService: RepostStateCommandService,
    private val reportStateQueryService: ReportStateQueryService
) {

    private fun flushAndClear() {
        entityManager.flush()
        entityManager.clear()
    }

    @Test
    @Transactional
    fun createRepostState() {
        //given
        val email = "test_member@gmail.com"
        val pw = "1234"
        val nickName = "member"
        val request = SignupRequest(email, pw, nickName)

        //when
        memberCommandService.signupMember(request)
        flushAndClear()

        //then
        val loginRequest = LoginRequest(email, pw)
        val memberUUID = memberCommandService.login(loginRequest).uuid
        Assertions.assertThat(reportStateQueryService.getOneByMemberUUID(memberUUID).memberState)
            .isEqualTo(MemberState.NORMAL)
    }

    @Test
    @Transactional
    fun releaseSuspend() {
        //given
        val email = "test_member@gmail.com"
        val pw = "1234"
        val nickName = "member"
        val request = SignupRequest(email, pw, nickName)
        memberCommandService.signupMember(request)
        flushAndClear()
        val memberUUID = memberCommandService.login(LoginRequest(email, pw)).uuid

        //when
        repeat(3) {
            val dtoRequest = ReportMember(memberUUID)
            repostStateCommandService.addRepost(dtoRequest)
            flushAndClear()
        }
        val loginRequest = LoginRequest(email, pw)

        //then
        Assertions.assertThatThrownBy { memberCommandService.login(loginRequest) }
            .isInstanceOf(InternalAuthenticationServiceException::class.java)
    }

    @Test
    @Transactional
    fun addRepost() {
        //given
        val email = "test_member@gmail.com"
        val pw = "1234"
        val nickName = "member"
        val request = SignupRequest(email, pw, nickName)
        memberCommandService.signupMember(request)
        flushAndClear()
        val memberUUID = memberCommandService.login(LoginRequest(email, pw)).uuid

        //when
        repeat(3) {
            val dtoRequest = ReportMember(memberUUID)
            repostStateCommandService.addRepost(dtoRequest)
            flushAndClear()
        }

        //then
        Assertions.assertThat(reportStateQueryService.getOneByMemberUUID(memberUUID).memberState)
            .isEqualTo(MemberState.SUSPEND_MONTH)
    }
}