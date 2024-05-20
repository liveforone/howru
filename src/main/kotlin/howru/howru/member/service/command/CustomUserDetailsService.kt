package howru.howru.member.service.command

import howru.howru.member.exception.MemberException
import howru.howru.member.exception.MemberExceptionMessage
import howru.howru.logger
import howru.howru.member.domain.Member
import howru.howru.member.domain.Role
import howru.howru.member.log.MemberServiceLog
import howru.howru.member.repository.MemberCustomRepository
import howru.howru.reportState.service.command.ReportStateCommandService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService
    @Autowired
    constructor(
        private val reportStateCommandService: ReportStateCommandService,
        private val memberRepository: MemberCustomRepository
    ) : UserDetailsService {
        override fun loadUserByUsername(email: String): UserDetails {
            val reportState = reportStateCommandService.releaseSuspend(email)
            check(reportState.isNotSuspend()) {
                logger().warn(MemberServiceLog.SUSPEND_MEMBER + email)
                throw MemberException(MemberExceptionMessage.SUSPEND_MEMBER, email)
            }
            val member = memberRepository.findMemberByEmail(email)
            return createUserDetails(member)
        }

        private fun createUserDetails(member: Member): UserDetails {
            return when (member.auth) {
                Role.ADMIN -> {
                    createAdmin(member)
                }
                else -> {
                    createMember(member)
                }
            }
        }

        private fun createAdmin(member: Member): UserDetails {
            return User.builder()
                .username(member.id.toString())
                .password(member.password)
                .roles(Role.ADMIN.name)
                .build()
        }

        private fun createMember(member: Member): UserDetails {
            return User.builder()
                .username(member.id.toString())
                .password(member.password)
                .roles(Role.MEMBER.name)
                .build()
        }
    }
