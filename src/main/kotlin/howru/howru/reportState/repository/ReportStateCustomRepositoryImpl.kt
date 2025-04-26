package howru.howru.reportState.repository

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import howru.howru.reportState.exceptioin.ReportStateException
import howru.howru.reportState.exceptioin.RepostStateExceptionMessage
import howru.howru.member.domain.QMember
import howru.howru.reportState.domain.QReportState
import howru.howru.reportState.domain.ReportState
import howru.howru.reportState.dto.ReportStateInfo
import java.util.*

class ReportStateCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val reportState: QReportState = QReportState.reportState,
    private val member: QMember = QMember.member
) : ReportStateCustomRepository {
    override fun findReportStateByMemberEmail(email: String): ReportState =
        jpaQueryFactory
            .selectFrom(reportState)
            .join(reportState.member, member)
            .where(member.email.eq(email))
            .fetchOne() ?: throw ReportStateException(RepostStateExceptionMessage.REPORT_STATE_IS_NULL, email)

    override fun findReportStateByMemberId(memberId: UUID): ReportState =
        jpaQueryFactory
            .selectFrom(reportState)
            .join(reportState.member, member)
            .where(member.id.eq(memberId))
            .fetchOne() ?: throw ReportStateException(
            RepostStateExceptionMessage.REPORT_STATE_IS_NULL,
            memberId.toString()
        )

    override fun findReportStateInfoByMemberId(memberId: UUID): ReportStateInfo =
        jpaQueryFactory
            .select(
                Projections.constructor(
                    ReportStateInfo::class.java,
                    reportState.memberState,
                    reportState.modifiedStateDate,
                    reportState.reportCount
                )
            ).from(reportState)
            .where(reportState.member.id.eq(memberId))
            .fetchOne() ?: throw ReportStateException(
            RepostStateExceptionMessage.REPORT_STATE_IS_NULL,
            memberId.toString()
        )
}
