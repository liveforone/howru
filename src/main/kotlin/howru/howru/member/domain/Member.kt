package howru.howru.member.domain

import howru.howru.converter.MemberLockConverter
import howru.howru.converter.RoleConverter
import howru.howru.exception.exception.MemberException
import howru.howru.exception.message.MemberExceptionMessage
import howru.howru.globalUtil.UUID_TYPE
import howru.howru.globalUtil.createUUID
import howru.howru.member.domain.constant.MemberConstant
import howru.howru.member.domain.util.PasswordUtil
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Entity
class Member private constructor(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long?,
    @Column(columnDefinition = UUID_TYPE, unique = true, nullable = false) val uuid: UUID = createUUID(),
    @Convert(converter = RoleConverter::class) @Column(nullable = false) var auth: Role,
    @Column(nullable = false) var email: String,
    @Column(nullable = false) var pw: String,
    @Convert(converter = MemberLockConverter::class) @Column(nullable = false) var memberLock: MemberLock = MemberLock.OFF,
    @Column(nullable = false) var reportCount: Long = MemberConstant.BASIC_REPORT,
) : UserDetails {
    companion object {
        private fun isAdmin(email: String) = (email == MemberConstant.ADMIN_EMAIL)

        fun create(email: String, pw: String, auth: Role): Member {
            return Member(
                id = null,
                auth = if (isAdmin(email)) Role.ADMIN else auth,
                email = email,
                pw = PasswordUtil.encodePassword(pw)
            )
        }
    }

    fun isNotSuspend() = auth != Role.SUSPEND

    fun isAdmin() = auth == Role.ADMIN

    fun updateEmail(newEmail: String) {
        email = newEmail
    }

    fun updatePw(newPassword: String, oldPassword: String) {
        require (PasswordUtil.isMatchPassword(oldPassword, pw)) { throw MemberException(MemberExceptionMessage.WRONG_PASSWORD) }
        pw = PasswordUtil.encodePassword(newPassword)
    }

    fun lockOn() {
        memberLock = MemberLock.ON
    }

    fun lockOff() {
        memberLock = MemberLock.OFF
    }

    fun addReport() {
        if (reportCount > MemberConstant.SUSPEND_LIMIT) auth = Role.SUSPEND
        else reportCount += MemberConstant.BASIC_VARIATION
    }


    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return arrayListOf<GrantedAuthority>(SimpleGrantedAuthority(auth.auth))
    }
    override fun getUsername() = uuid.toString()
    override fun getPassword() = pw
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}