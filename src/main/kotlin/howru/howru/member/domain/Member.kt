package howru.howru.member.domain

import howru.howru.converter.MemberLockConverter
import howru.howru.converter.RoleConverter
import howru.howru.exception.exception.MemberException
import howru.howru.exception.message.MemberExceptionMessage
import howru.howru.globalUtil.UUID_TYPE
import howru.howru.globalUtil.createUUID
import howru.howru.globalUtil.encodePassword
import howru.howru.globalUtil.isMatchPassword
import howru.howru.member.domain.constant.MemberConstant
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Entity
class Member private constructor(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @Column(columnDefinition = UUID_TYPE, unique = true, nullable = false) val uuid: UUID = createUUID(),
    @Convert(converter = RoleConverter::class) @Column(
        nullable = false,
        columnDefinition = MemberConstant.ROLE_TYPE
    ) var auth: Role,
    @Column(nullable = false) var email: String,
    @Column(nullable = false, columnDefinition = MemberConstant.PW_TYPE) var pw: String,
    @Column(nullable = false, unique = true, columnDefinition = MemberConstant.NICKNAME_TYPE) val nickName: String,
    @Convert(converter = MemberLockConverter::class) @Column(
        nullable = false,
        columnDefinition = MemberConstant.LOCK_TYPE
    ) var memberLock: MemberLock = MemberLock.OFF
) : UserDetails {
    companion object {
        private fun findFitAuth(email: String) = if (email == MemberConstant.ADMIN_EMAIL) Role.ADMIN else Role.MEMBER

        fun create(email: String, pw: String, nickName: String): Member {
            return Member(
                auth = findFitAuth(email),
                email = email,
                pw = encodePassword(pw),
                nickName = nickName
            )
        }
    }

    fun isAdmin() = auth == Role.ADMIN

    fun updateEmail(newEmail: String) {
        email = newEmail
    }

    fun updatePw(newPassword: String, oldPassword: String) {
        require (isMatchPassword(oldPassword, pw)) { throw MemberException(MemberExceptionMessage.WRONG_PASSWORD, uuid.toString()) }
        pw = encodePassword(newPassword)
    }

    fun lockOn() {
        memberLock = MemberLock.ON
    }

    fun lockOff() {
        memberLock = MemberLock.OFF
    }

    fun withdraw() {
        this.auth = Role.WITHDRAW
    }

    fun recovery(inputPw: String) {
        require (isMatchPassword(inputPw, pw)) { throw MemberException(MemberExceptionMessage.WRONG_PASSWORD, uuid.toString()) }
        this.auth = Role.MEMBER
    }


    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        arrayListOf<GrantedAuthority>(SimpleGrantedAuthority(auth.auth))
    override fun getUsername() = uuid.toString()
    override fun getPassword() = pw
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}