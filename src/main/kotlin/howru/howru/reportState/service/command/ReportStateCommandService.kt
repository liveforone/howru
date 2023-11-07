package howru.howru.reportState.service.command

import howru.howru.logger
import howru.howru.member.domain.Member
import howru.howru.reportState.domain.ReportState
import howru.howru.reportState.dto.request.ReportMember
import howru.howru.reportState.repository.ReportStateQuery
import howru.howru.reportState.service.command.log.ReportLog
import howru.howru.reportState.repository.ReportStateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReportStateCommandService @Autowired constructor(
    private val reportStateRepository: ReportStateRepository,
    private val reportStateQuery: ReportStateQuery
) {
    fun createRepostState(member: Member) {
        reportStateRepository.save(ReportState.create(member))
    }

    fun releaseSuspend(email: String): ReportState {
        return reportStateQuery.findOneByMemberEmail(email)
            .also { it.releaseSuspend() }
    }

    fun addRepost(reportMember: ReportMember) {
        with(reportMember) {
            reportStateQuery.findOneByMemberUUID(memberUUID!!)
                .also { it.addReport() }
            logger().info(ReportLog.REPORT_MEMBER.log + memberUUID)
        }
    }
}