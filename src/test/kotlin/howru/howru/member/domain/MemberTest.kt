package howru.howru.member.domain

import howru.howru.member.domain.util.PasswordUtil
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class MemberTest {

    @Test
    fun updateEmailTest() {
        //given
        val email = "email_test@gmail.com"
        val pw = "1234"
        val member = Member.create(email, pw, Role.MEMBER)

        //when
        val updatedEmail = "updated_email@gmail.com"
        member.updateEmail(updatedEmail)

        //then
        Assertions.assertThat(member.email).isEqualTo(updatedEmail)
    }

    @Test
    fun updatePwTest() {
        //given
        val email = "pw_test@gmail.com"
        val pw = "1234"
        val member = Member.create(email, pw, Role.MEMBER)

        //when
        val updatedPw = "1111"
        member.updatePw(updatedPw, pw)

        //then
        Assertions.assertThat(PasswordUtil.isMatchPassword(updatedPw, member.pw)).isTrue()
    }

    @Test
    fun lockOnTest() {
        //given
        val email = "lock_on_test@gmail.com"
        val pw = "1234"
        val member = Member.create(email, pw, Role.MEMBER)

        //when
        member.lockOn()

        //then
        Assertions.assertThat(member.memberLock).isEqualTo(MemberLock.ON)
    }

    @Test
    fun lockOffTest() {
        //given
        val email = "lock_off_test@gmail.com"
        val pw = "1234"
        val member = Member.create(email, pw, Role.MEMBER)
        member.lockOn()

        //when
        member.lockOff()

        //then
        Assertions.assertThat(member.memberLock).isEqualTo(MemberLock.OFF)
    }

    @Test
    fun addReportTest() {
        //given
        val email = "pw_test@gmail.com"
        val pw = "1234"
        val member = Member.create(email, pw, Role.MEMBER)

        //when
        member.addReport()

        //then
        Assertions.assertThat(member.reportCount).isEqualTo(1)
    }
}