package howru.howru.member.service.command

import howru.howru.member.domain.MemberLock
import howru.howru.member.domain.Role
import howru.howru.member.dto.request.SignupRequest
import howru.howru.member.dto.update.UpdateEmail
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
    private val memberQueryService: MemberQueryService
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
        val request = SignupRequest(email, pw)

        //when
        val uuid = memberCommandService.signupMember(request)
        flushAndClear()

        //then
        Assertions.assertThat(memberQueryService.getMemberByUUID(uuid).auth)
            .isEqualTo(Role.MEMBER)
    }

    @Test
    @Transactional
    fun updateEmailTest() {
        //given
        val email = "email_test@gmail.com"
        val pw = "1234"
        val request = SignupRequest(email, pw)
        val uuid = memberCommandService.signupMember(request)
        flushAndClear()

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
        val request = SignupRequest(email, pw)
        val uuid = memberCommandService.signupMember(request)
        flushAndClear()

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
        val request = SignupRequest(email, pw)
        val uuid = memberCommandService.signupMember(request)
        flushAndClear()
        memberCommandService.memberLockOn(uuid)
        flushAndClear()

        //when
        memberCommandService.memberLockOff(uuid)
        flushAndClear()

        //then
        Assertions.assertThat(memberQueryService.getMemberByUUID(uuid).memberLock)
            .isEqualTo(MemberLock.OFF)
    }

    @Test
    @Transactional
    fun addReportCountTest() {
        //given
        val email = "email_test@gmail.com"
        val pw = "1234"
        val request = SignupRequest(email, pw)
        val uuid = memberCommandService.signupMember(request)
        flushAndClear()

        //when
        memberCommandService.addReportCount(uuid)
        flushAndClear()

        //then
        Assertions.assertThat(memberQueryService.getMemberByUUID(uuid).reportCount)
            .isEqualTo(1)
    }
}