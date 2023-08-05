package howru.howru.report.handler

import howru.howru.logger
import howru.howru.member.service.command.MemberCommandService
import howru.howru.report.dto.ReportMember
import howru.howru.report.handler.constant.ReportConstant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ReportHandler @Autowired constructor(
    private val memberCommandService: MemberCommandService
) {
    fun reportMember(reportMember: ReportMember) {
        with(reportMember) {
            memberCommandService.addReportCount(memberUUID!!)
            logger().info(ReportConstant.REPORT_MEMBER + memberUUID)
        }
    }
}