package howru.howru.member.service.command

import howru.howru.jwt.exception.JwtCustomException
import howru.howru.member.exception.MemberException
import howru.howru.jwt.service.JwtTokenService
import howru.howru.member.domain.MemberLock
import howru.howru.member.domain.Role
import howru.howru.member.dto.request.*
import howru.howru.member.service.query.MemberQueryService
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class MemberCommandServiceTest
    @Autowired
    constructor(
        private val entityManager: EntityManager,
        private val memberCommandService: MemberCommandService,
        private val memberQueryService: MemberQueryService,
        private val jwtTokenService: JwtTokenService
    ) {
        private fun flushAndClear() {
            entityManager.flush()
            entityManager.clear()
        }

        @Test
        @Transactional
        fun signupTest() {
            // given
            val email = "signup_test@gmail.com"
            val pw = "1234"
            val nickName = "nickName"
            val request = SignupRequest(email, pw, nickName)

            // when
            memberCommandService.signup(request)
            flushAndClear()

            // then
            val loginRequest = LoginRequest(email, pw)
            val jwtTokenInfo = memberCommandService.login(loginRequest)
            Assertions.assertThat(memberQueryService.getMemberById(jwtTokenInfo.id).auth)
                .isEqualTo(Role.MEMBER)
        }

        @Test
        @Transactional
        fun updatePasswordTest() {
            // given
            val email = "update_password_test@gmail.com"
            val pw = "1234"
            val nickName = "nickName"
            val request = SignupRequest(email, pw, nickName)
            memberCommandService.signup(request)
            flushAndClear()
            val loginRequest = LoginRequest(email, pw)
            val id = memberCommandService.login(loginRequest).id

            // when
            val newPw = "1111"
            memberCommandService.updatePassword(UpdatePassword(newPw, pw), id)
            flushAndClear()

            // then
            Assertions.assertThat(memberCommandService.login(LoginRequest(email, newPw)).id).isEqualTo(id)
        }

        @Test
        @Transactional
        fun reissueJwtTokenTest() {
            // given
            val email = "reissue_token_test@gmail.com"
            val pw = "1234"
            val nickName = "nickName"
            val request = SignupRequest(email, pw, nickName)
            memberCommandService.signup(request)
            flushAndClear()
            val loginRequest = LoginRequest(email, pw)
            val jwtTokenInfo = memberCommandService.login(loginRequest)

            // when
            val reissueJwtToken = memberCommandService.reissueJwtToken(jwtTokenInfo.id, jwtTokenInfo.refreshToken)
            flushAndClear()

            // then
            val jwtTokenInfo2 = memberCommandService.login(loginRequest)
            Assertions.assertThat(reissueJwtToken.refreshToken.equals(jwtTokenInfo2.refreshToken)).isTrue()
        }

        @Test
        @Transactional
        fun memberLockOnTest() {
            // given
            val email = "lock_on_test@gmail.com"
            val pw = "1234"
            val nickName = "nickName"
            val request = SignupRequest(email, pw, nickName)
            memberCommandService.signup(request)
            flushAndClear()
            val loginRequest = LoginRequest(email, pw)
            val id = memberCommandService.login(loginRequest).id

            // when
            memberCommandService.memberLockOn(id)
            flushAndClear()

            // then
            Assertions.assertThat(memberQueryService.getMemberById(id).memberLock)
                .isEqualTo(MemberLock.ON)
        }

        @Test
        @Transactional
        fun memberLockOffTest() {
            // given
            val email = "lock_off_test@gmail.com"
            val pw = "1234"
            val nickName = "nickName"
            val request = SignupRequest(email, pw, nickName)
            memberCommandService.signup(request)
            flushAndClear()
            val loginRequest = LoginRequest(email, pw)
            val id = memberCommandService.login(loginRequest).id
            memberCommandService.memberLockOn(id)
            flushAndClear()

            // when
            memberCommandService.memberLockOff(id)
            flushAndClear()

            // then
            Assertions.assertThat(memberQueryService.getMemberById(id).memberLock)
                .isEqualTo(MemberLock.OFF)
        }

        @Test
        @Transactional
        fun recoveryTest() {
            // given
            val email = "recovery_test@gmail.com"
            val pw = "1234"
            val nickName = "nickName"
            val request = SignupRequest(email, pw, nickName)
            memberCommandService.signup(request)
            flushAndClear()
            val loginRequest = LoginRequest(email, pw)
            val id = memberCommandService.login(loginRequest).id
            val withdrawRequest = WithdrawRequest(pw)
            memberCommandService.withdraw(withdrawRequest, id)
            flushAndClear()

            // when
            memberCommandService.recoveryMember(RecoveryRequest(email, pw))
            flushAndClear()

            // then
            Assertions.assertThat(memberQueryService.getMemberById(id)).isNotNull
        }

    /*
     * 회원 탈퇴후 해당 회원을 다시 조회하게 되면, MemberException(MEMBER_IS_NULL) 예외가 발생하게 됩니다.
     */
        @Test
        @Transactional
        fun withdrawTest() {
            // given
            val email = "withdraw_test@gmail.com"
            val pw = "1234"
            val nickName = "nickName"
            val request = SignupRequest(email, pw, nickName)
            memberCommandService.signup(request)
            flushAndClear()
            val loginRequest = LoginRequest(email, pw)
            val id = memberCommandService.login(loginRequest).id

            // when
            val withdrawRequest = WithdrawRequest(pw)
            memberCommandService.withdraw(withdrawRequest, id)
            flushAndClear()

            // then
            Assertions.assertThatThrownBy { jwtTokenService.getRefreshToken(id) }
                .isInstanceOf(JwtCustomException::class.java)
            Assertions.assertThatThrownBy { (memberQueryService.getMemberById(id)) }
                .isInstanceOf(MemberException::class.java)
        }
    }
