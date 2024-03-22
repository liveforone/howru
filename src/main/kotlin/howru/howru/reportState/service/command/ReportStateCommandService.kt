package howru.howru.reportState.service.command

import howru.howru.member.domain.Member
import howru.howru.reportState.domain.ReportState
import howru.howru.reportState.dto.request.ReportMember
import howru.howru.reportState.repository.ReportStateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReportStateCommandService @Autowired constructor(
    private val reportStateRepository: ReportStateRepository
) {
    fun createRepostState(member: Member) {
        reportStateRepository.save(ReportState.create(member))
    }

    fun releaseSuspend(email: String): ReportState {
        return reportStateRepository.findReportStateByMemberEmail(email).also { it.releaseSuspend() }
    }

    fun addRepost(reportMember: ReportMember) {
        with(reportMember) {
            reportStateRepository.findReportStateByMemberId(memberId!!).also { it.addReport() }
        }
    }
}