package howru.howru.member.repository

import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.listQuery
import com.linecorp.kotlinjdsl.spring.data.singleQuery
import howru.howru.exception.exception.MemberException
import howru.howru.exception.message.MemberExceptionMessage
import howru.howru.member.domain.Member
import howru.howru.member.dto.response.MemberInfo
import howru.howru.member.repository.constant.MemberRepositoryConstant
import jakarta.persistence.NoResultException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class MemberRepositoryImpl @Autowired constructor(
    private val queryFactory: SpringDataQueryFactory
) : MemberCustomRepository {

    override fun findIdByEmailNullableForValidate(email: String): Long? {
        val foundIds = queryFactory.listQuery<Long> {
            select(listOf(col(Member::id)))
            from(entity(Member::class))
            where(col(Member::email).equal(email))
        }
        return if (foundIds.isEmpty()) null else foundIds[MemberRepositoryConstant.FIRST_INDEX]
    }

    override fun findOneByEmail(email: String): Member {
        return try {
            queryFactory.singleQuery {
                select(entity(Member::class))
                from(entity(Member::class))
                where(col(Member::email).equal(email))
            }
        } catch (e: NoResultException) {
            throw MemberException(MemberExceptionMessage.MEMBER_IS_NULL)
        }
    }

    override fun findOneByUUID(uuid: UUID): Member {
        return try {
            queryFactory.singleQuery {
                select(entity(Member::class))
                from(entity(Member::class))
                where(col(Member::uuid).equal(uuid))
            }
        } catch (e: NoResultException) {
            throw MemberException(MemberExceptionMessage.MEMBER_IS_NULL)
        }
    }

    override fun findOneDtoByUUID(uuid: UUID): MemberInfo {
        return try {
            queryFactory.singleQuery {
                select(listOf(
                    col(Member::uuid),
                    col(Member::auth),
                    col(Member::email),
                    col(Member::reportCount)
                ))
                from(entity(Member::class))
                where(col(Member::uuid).equal(uuid))
            }
        } catch (e: NoResultException) {
            throw MemberException(MemberExceptionMessage.MEMBER_IS_NULL)
        }
    }
}