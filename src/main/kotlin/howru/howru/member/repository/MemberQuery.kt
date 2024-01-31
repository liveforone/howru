package howru.howru.member.repository

import howru.howru.exception.exception.MemberException
import howru.howru.exception.message.MemberExceptionMessage
import howru.howru.member.domain.Member
import howru.howru.member.domain.Role
import howru.howru.member.dto.response.MemberInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class MemberQuery @Autowired constructor(
    private val memberRepository: MemberRepository
) {

    fun findOneByEmail(email: String): Member {
        return memberRepository.findAll {
            select(entity(Member::class))
                .from(entity(Member::class))
                .where(path(Member::email).eq(email).and(path(Member::auth).notEqual(Role.WITHDRAW)))
        }.firstOrNull() ?: throw MemberException(MemberExceptionMessage.MEMBER_IS_NULL, email)
    }

    fun findOneById(id: UUID): Member {
        return memberRepository.findAll {
            select(entity(Member::class))
                .from(entity(Member::class))
                .where(path(Member::id).eq(id).and(path(Member::auth).notEqual(Role.WITHDRAW)))
        }.firstOrNull() ?: throw MemberException(MemberExceptionMessage.MEMBER_IS_NULL, id.toString())
    }

    fun findOneByEmailAllowWithdraw(email: String): Member {
        return memberRepository.findAll {
            select(entity(Member::class))
                .from(entity(Member::class))
                .where(path(Member::email).eq(email))
        }.firstOrNull() ?: throw MemberException(MemberExceptionMessage.MEMBER_IS_NULL, email)
    }

    fun findOneDtoById(id: UUID): MemberInfo {
        return memberRepository.findAll {
            selectNew<MemberInfo>(
                path(Member::id),
                path(Member::auth),
                path(Member::email),
                path(Member::nickName),
                path(Member::memberLock)
            ).from(entity(Member::class))
                .where(path(Member::id).eq(id).and(path(Member::auth).notEqual(Role.WITHDRAW)))
        }.firstOrNull() ?: throw MemberException(MemberExceptionMessage.MEMBER_IS_NULL, id.toString())
    }

    fun findAuthById(id: UUID): Role {
        return memberRepository.findAll {
            select(path(Member::auth))
                .from(entity(Member::class))
                .where(path(Member::id).eq(id).and(path(Member::auth).notEqual(Role.WITHDRAW)))
        }.firstOrNull() ?: throw MemberException(MemberExceptionMessage.MEMBER_IS_NULL, id.toString())
    }
}