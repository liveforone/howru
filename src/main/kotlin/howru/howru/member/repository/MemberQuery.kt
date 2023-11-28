package howru.howru.member.repository

import howru.howru.exception.exception.MemberException
import howru.howru.exception.message.MemberExceptionMessage
import howru.howru.member.domain.Member
import howru.howru.member.dto.response.MemberInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class MemberQuery @Autowired constructor(
    private val memberRepository: MemberRepository
) {
    fun findIdByEmailNullableForValidate(email: String): Long? {
        return memberRepository.findAll {
            selectNew<Long>(path(Member::id))
                .from(entity(Member::class))
                .where(path(Member::email).eq(email))
        }.firstOrNull()
    }

    fun findOneByEmail(email: String): Member {
        return memberRepository.findAll {
            select(entity(Member::class))
                .from(entity(Member::class))
                .where(path(Member::email).eq(email))
        }.firstOrNull() ?: throw MemberException(MemberExceptionMessage.MEMBER_IS_NULL, email)
    }

    fun findOneByUUID(uuid: UUID): Member {
        return memberRepository.findAll {
            select(entity(Member::class))
                .from(entity(Member::class))
                .where(path(Member::uuid).eq(uuid))
        }.firstOrNull() ?: throw MemberException(MemberExceptionMessage.MEMBER_IS_NULL, uuid.toString())
    }

    fun findOneDtoByUUID(uuid: UUID): MemberInfo {
        return memberRepository.findAll {
            selectNew<MemberInfo>(
                path(Member::uuid),
                path(Member::auth),
                path(Member::email),
                path(Member::nickName),
                path(Member::memberLock)
            ).from(entity(Member::class))
                .where(path(Member::uuid).eq(uuid))
        }.firstOrNull() ?: throw MemberException(MemberExceptionMessage.MEMBER_IS_NULL, uuid.toString())
    }
}