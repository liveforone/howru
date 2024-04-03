package howru.howru.member.repository

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import howru.howru.exception.exception.MemberException
import howru.howru.exception.message.MemberExceptionMessage
import howru.howru.member.domain.Member
import howru.howru.member.domain.QMember
import howru.howru.member.domain.Role
import howru.howru.member.dto.response.MemberInfo
import java.util.*

class MemberCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val member: QMember = QMember.member
) : MemberCustomRepository {
    override fun findMemberByEmail(email: String): Member {
        return jpaQueryFactory.selectFrom(member)
            .where(member.email.eq(email).and(member.auth.ne(Role.WITHDRAW)))
            .fetchOne() ?: throw MemberException(MemberExceptionMessage.MEMBER_IS_NULL, email)
    }

    override fun findMemberById(id: UUID): Member {
        return jpaQueryFactory.selectFrom(member)
            .where(member.id.eq(id).and(member.auth.ne(Role.WITHDRAW)))
            .fetchOne() ?: throw MemberException(MemberExceptionMessage.MEMBER_IS_NULL, id.toString())
    }

    override fun findMemberByEmailIncludeWithdraw(email: String): Member {
        return jpaQueryFactory.selectFrom(member)
            .where(member.email.eq(email))
            .fetchOne() ?: throw MemberException(MemberExceptionMessage.MEMBER_IS_NULL, email)
    }

    override fun findMemberInfoById(id: UUID): MemberInfo {
        return jpaQueryFactory.select(
            Projections.constructor(
                MemberInfo::class.java,
                member.id,
                member.auth,
                member.email,
                member.nickName,
                member.memberLock
            )
        )
            .from(member)
            .where(member.id.eq(id).and(member.auth.ne(Role.WITHDRAW)))
            .fetchOne() ?: throw MemberException(MemberExceptionMessage.MEMBER_IS_NULL, id.toString())
    }

    override fun findAuthById(id: UUID): Role {
        return jpaQueryFactory.select(member.auth)
            .from(member)
            .where(member.id.eq(id).and(member.auth.ne(Role.WITHDRAW)))
            .fetchOne() ?: throw MemberException(MemberExceptionMessage.MEMBER_IS_NULL, id.toString())
    }
}
