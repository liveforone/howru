package howru.howru.member.service.command

import howru.howru.exception.exception.JwtCustomException
import howru.howru.exception.exception.MemberException
import howru.howru.jwt.service.JwtTokenService
import howru.howru.member.domain.MemberLock
import howru.howru.member.domain.Role
import howru.howru.member.dto.request.LoginRequest
import howru.howru.member.dto.request.SignupRequest
import howru.howru.member.dto.request.UpdateEmail
import howru.howru.member.dto.request.WithdrawRequest
import howru.howru.member.service.query.MemberQueryService
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class MemberCommandServiceTest @Autowired constructor(
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
    fun signupMemberTest() {
        //given
        val email = "signup_test@gmail.com"
        val pw = "1234"
        val nickName = "nickName"
        val request = SignupRequest(email, pw, nickName)

        //when
        memberCommandService.signupMember(request)
        flushAndClear()

        //then
        val loginRequest = LoginRequest(email, pw)
        val jwtTokenInfo = memberCommandService.login(loginRequest)
        Assertions.assertThat(memberQueryService.getMemberByUUID(jwtTokenInfo.uuid).auth)
            .isEqualTo(Role.MEMBER)
    }

    @Test
    @Transactional
    fun reissueJwtToken() {
        //given
        val email = "reissue_token_test@gmail.com"
        val pw = "1234"
        val nickName = "nickName"
        val request = SignupRequest(email, pw, nickName)
        memberCommandService.signupMember(request)
        flushAndClear()
        val loginRequest = LoginRequest(email, pw)
        val jwtTokenInfo = memberCommandService.login(loginRequest)

        //when
        val reissueJwtToken = memberCommandService.reissueJwtToken(jwtTokenInfo.uuid, jwtTokenInfo.refreshToken)
        flushAndClear()

        //then
        val jwtTokenInfo2 = memberCommandService.login(loginRequest)
        Assertions.assertThat(reissueJwtToken.refreshToken).isEqualTo(jwtTokenInfo2.refreshToken)
    }

    @Test
    @Transactional
    fun updateEmailTest() {
        //given
        val email = "email_test@gmail.com"
        val pw = "1234"
        val nickName = "nickName"
        val request = SignupRequest(email, pw, nickName)
        memberCommandService.signupMember(request)
        flushAndClear()
        val loginRequest = LoginRequest(email, pw)
        val uuid = memberCommandService.login(loginRequest).uuid

        //when
        val newEmail = "updated_email@gmail.com"
        val updateRequest = UpdateEmail(newEmail)
        memberCommandService.updateEmail(updateRequest, uuid)
        flushAndClear()

        //then
        Assertions.assertThat(memberQueryService.getMemberByUUID(uuid).email)
            .isEqualTo(newEmail)
    }

    @Test
    @Transactional
    fun memberLockOnTest() {
        //given
        val email = "lock_on_test@gmail.com"
        val pw = "1234"
        val nickName = "nickName"
        val request = SignupRequest(email, pw, nickName)
        memberCommandService.signupMember(request)
        flushAndClear()
        val loginRequest = LoginRequest(email, pw)
        val uuid = memberCommandService.login(loginRequest).uuid

        //when
        memberCommandService.memberLockOn(uuid)
        flushAndClear()

        //then
        Assertions.assertThat(memberQueryService.getMemberByUUID(uuid).memberLock)
            .isEqualTo(MemberLock.ON)
    }

    @Test
    @Transactional
    fun memberLockOffTest() {
        //given
        val email = "lock_off_test@gmail.com"
        val pw = "1234"
        val nickName = "nickName"
        val request = SignupRequest(email, pw, nickName)
        memberCommandService.signupMember(request)
        flushAndClear()
        val loginRequest = LoginRequest(email, pw)
        val uuid = memberCommandService.login(loginRequest).uuid
        memberCommandService.memberLockOn(uuid)
        flushAndClear()

        //when
        memberCommandService.memberLockOff(uuid)
        flushAndClear()

        //then
        Assertions.assertThat(memberQueryService.getMemberByUUID(uuid).memberLock)
            .isEqualTo(MemberLock.OFF)
    }

    /*
    * 회원 탈퇴후 해당 회원을 다시 조회하게 되면, MemberException(MEMBER_IS_NULL) 예외가 발생하게 됩니다.
     */
    @Test
    @Transactional
    fun withdrawTest() {
        //given
        val email = "withdraw_test@gmail.com"
        val pw = "1234"
        val nickName = "nickName"
        val request = SignupRequest(email, pw, nickName)
        memberCommandService.signupMember(request)
        flushAndClear()
        val loginRequest = LoginRequest(email, pw)
        val uuid = memberCommandService.login(loginRequest).uuid

        //when
        val withdrawRequest = WithdrawRequest(pw)
        memberCommandService.withdraw(withdrawRequest, uuid)
        flushAndClear()

        //then
        Assertions.assertThatThrownBy { jwtTokenService.getRefreshToken(uuid) }
            .isInstanceOf(JwtCustomException::class.java)
        Assertions.assertThatThrownBy { (memberQueryService.getMemberByUUID(uuid)) }
            .isInstanceOf(MemberException::class.java)
    }
}