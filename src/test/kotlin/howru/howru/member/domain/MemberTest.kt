package howru.howru.member.domain

import howru.howru.globalUtil.isMatchPassword
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class MemberTest {

    @Test
    fun createAdminTest() {
        //given
        val email = "admin_howru@gmail.com"
        val pw = "1234"
        val nickName = "nickName"

        //when
        val member = Member.create(email, pw, nickName)

        //then
        Assertions.assertThat(member.auth).isEqualTo(Role.ADMIN)
    }

    @Test
    fun updateEmailTest() {
        //given
        val email = "email_test@gmail.com"
        val pw = "1234"
        val nickName = "nickName"
        val member = Member.create(email, pw, nickName)

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
        val nickName = "nickName"
        val member = Member.create(email, pw, nickName)

        //when
        val updatedPw = "1111"
        member.updatePw(updatedPw, pw)

        //then
        Assertions.assertThat(isMatchPassword(updatedPw, member.pw)).isTrue()
    }

    @Test
    fun lockOnTest() {
        //given
        val email = "lock_on_test@gmail.com"
        val pw = "1234"
        val nickName = "nickName"
        val member = Member.create(email, pw, nickName)

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
        val nickName = "nickName"
        val member = Member.create(email, pw, nickName)
        member.lockOn()

        //when
        member.lockOff()

        //then
        Assertions.assertThat(member.memberLock).isEqualTo(MemberLock.OFF)
    }
}