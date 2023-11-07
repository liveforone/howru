package howru.howru.reportState.service.command

import howru.howru.logger
import howru.howru.member.domain.Member
import howru.howru.reportState.domain.ReportState
import howru.howru.reportState.dto.request.ReportMember
import howru.howru.reportState.service.command.log.ReportLog
import howru.howru.reportState.repository.RepostStateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReportStateCommandService @Autowired constructor(
    private val repostStateRepository: RepostStateRepository
) {
    fun createRepostState(member: Member) {
        repostStateRepository.save(ReportState.create(member))
    }

    fun releaseSuspend(email: String): ReportState {
        return repostStateRepository.findOneByMemberEmail(email)
            .also { it.releaseSuspend() }
    }

    fun addRepost(reportMember: ReportMember) {
        with(reportMember) {
            repostStateRepository.findOneByMemberUUID(memberUUID!!)
                .also { it.addReport() }
            logger().info(ReportLog.REPORT_MEMBER.log + memberUUID)
        }
    }
}