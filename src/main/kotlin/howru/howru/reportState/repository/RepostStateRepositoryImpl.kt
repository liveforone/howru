package howru.howru.reportState.repository

import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.from.join
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.singleQuery
import howru.howru.exception.exception.ReportStateException
import howru.howru.exception.message.RepostStateExceptionMessage
import howru.howru.member.domain.Member
import howru.howru.reportState.domain.ReportState
import howru.howru.reportState.dto.response.RepostStateInfo
import jakarta.persistence.NoResultException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class RepostStateRepositoryImpl @Autowired constructor(
    private val queryFactory: SpringDataQueryFactory
) : RepostStateCustomRepository {
    override fun findOneByMemberEmail(email: String): ReportState {
        return try {
            queryFactory.singleQuery {
                select(entity(ReportState::class))
                from(ReportState::class)
                join(ReportState::member)
                where(col(Member::email).equal(email))
            }
        } catch (e: NoResultException) {
            throw ReportStateException(RepostStateExceptionMessage.REPORT_STATE_IS_NULL)
        }
    }

    override fun findOneByMemberUUID(memberUUID: UUID): ReportState {
        return try {
            queryFactory.singleQuery {
                select(entity(ReportState::class))
                from(ReportState::class)
                join(ReportState::member)
                where(col(Member::uuid).equal(memberUUID))
            }
        } catch (e: NoResultException) {
            throw ReportStateException(RepostStateExceptionMessage.REPORT_STATE_IS_NULL)
        }
    }

    override fun findOneDtoByMemberUUID(memberUUID: UUID): RepostStateInfo {
        return try {
            queryFactory.singleQuery {
                select(listOf(
                    col(ReportState::memberState),
                    col(ReportState::modifiedStateDate),
                    col(ReportState::reportCount)
                ))
                from(ReportState::class)
                join(ReportState::member)
                where(col(Member::uuid).equal(memberUUID))
            }
        } catch (e: NoResultException) {
            throw ReportStateException(RepostStateExceptionMessage.REPORT_STATE_IS_NULL)
        }
    }
}