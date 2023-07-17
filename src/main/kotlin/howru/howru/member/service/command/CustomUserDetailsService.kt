package howru.howru.member.service.command

import howru.howru.exception.exception.MemberException
import howru.howru.exception.message.MemberExceptionMessage
import howru.howru.member.domain.Member
import howru.howru.member.domain.Role
import howru.howru.member.repository.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService @Autowired constructor(
    private val memberRepository: MemberRepository
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val member = memberRepository.findOneByEmail(email)
        check(member.isNotSuspend()) { throw MemberException(MemberExceptionMessage.SUSPEND_MEMBER) }
        return createUserDetails(member)
    }

    private fun createUserDetails(member: Member): UserDetails {
        return when (member.auth) {
            Role.ADMIN -> { createAdmin(member) }
            else -> { createMember(member) }
        }
    }

    private fun createAdmin(member: Member): UserDetails {
        return User.builder()
            .username(member.uuid.toString())
            .password(member.password)
            .roles(Role.ADMIN.name)
            .build()
    }

    private fun createMember(member: Member): UserDetails {
        return User.builder()
            .username(member.uuid.toString())
            .password(member.password)
            .roles(Role.MEMBER.name)
            .build()
    }
}