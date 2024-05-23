package howru.howru.reportState.service.command

import howru.howru.member.dto.request.LoginRequest
import howru.howru.member.dto.request.SignupRequest
import howru.howru.member.service.command.MemberCommandService
import howru.howru.reportState.domain.MemberState
import howru.howru.reportState.dto.ReportMember
import howru.howru.reportState.service.query.ReportStateQueryService
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class ReportStateCommandServiceTest
    @Autowired
    constructor(
        private val entityManager: EntityManager,
        private val memberCommandService: MemberCommandService,
        private val reportStateCommandService: ReportStateCommandService,
        private val reportStateQueryService: ReportStateQueryService
    ) {
        private fun flushAndClear() {
            entityManager.flush()
            entityManager.clear()
        }

        @Test
        @Transactional
        fun createRepostState() {
            // given
            val email = "test_member@gmail.com"
            val pw = "1234"
            val nickName = "member"
            val request = SignupRequest(email, pw, nickName)

            // when
            memberCommandService.signup(request)
            flushAndClear()

            // then
            val loginRequest = LoginRequest(email, pw)
            val memberId = memberCommandService.login(loginRequest).id
            Assertions.assertThat(reportStateQueryService.getOneByMemberId(memberId).memberState)
                .isEqualTo(MemberState.NORMAL)
        }

        @Test
        @Transactional
        fun releaseSuspend() {
            // given
            val email = "test_member@gmail.com"
            val pw = "1234"
            val nickName = "member"
            val request = SignupRequest(email, pw, nickName)
            memberCommandService.signup(request)
            flushAndClear()
            val memberId = memberCommandService.login(LoginRequest(email, pw)).id

            // when
            repeat(3) {
                val dtoRequest = ReportMember(memberId)
                reportStateCommandService.addRepost(dtoRequest)
                flushAndClear()
            }
            val loginRequest = LoginRequest(email, pw)

            // then
            Assertions.assertThatThrownBy { memberCommandService.login(loginRequest) }
                .isInstanceOf(InternalAuthenticationServiceException::class.java)
        }

        @Test
        @Transactional
        fun addRepost() {
            // given
            val email = "test_member@gmail.com"
            val pw = "1234"
            val nickName = "member"
            val request = SignupRequest(email, pw, nickName)
            memberCommandService.signup(request)
            flushAndClear()
            val memberId = memberCommandService.login(LoginRequest(email, pw)).id

            // when
            repeat(3) {
                val dtoRequest = ReportMember(memberId)
                reportStateCommandService.addRepost(dtoRequest)
                flushAndClear()
            }

            // then
            Assertions.assertThat(reportStateQueryService.getOneByMemberId(memberId).memberState)
                .isEqualTo(MemberState.SUSPEND_MONTH)
        }
    }
