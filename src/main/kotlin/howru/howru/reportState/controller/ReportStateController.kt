package howru.howru.reportState.controller

import howru.howru.logger
import howru.howru.reportState.controller.constant.ReportStateParam
import howru.howru.reportState.controller.constant.ReportStateUrl
import howru.howru.reportState.controller.response.ReportStateResponse
import howru.howru.reportState.dto.ReportMember
import howru.howru.reportState.dto.ReportStateInfo
import howru.howru.reportState.log.ReportStateControllerLog
import howru.howru.reportState.service.command.ReportStateCommandService
import howru.howru.reportState.service.query.ReportStateQueryService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class ReportStateController
    @Autowired
    constructor(
        private val reportStateQueryService: ReportStateQueryService,
        private val reportStateCommandService: ReportStateCommandService
    ) {
        @GetMapping(ReportStateUrl.REPORT_STATE_INFO)
        fun getReportStateInfo(
            @PathVariable(ReportStateParam.MEMBER_ID) memberId: UUID
        ): ResponseEntity<ReportStateInfo> {
            val reportState = reportStateQueryService.getOneByMemberId(memberId)
            return ResponseEntity.ok(reportState)
        }

        @PostMapping(ReportStateUrl.REPORT)
        fun reportMember(
            @RequestBody @Valid reportMember: ReportMember
        ): ResponseEntity<String> {
            reportStateCommandService.addRepost(reportMember)
            logger().info(ReportStateControllerLog.REPORT_MEMBER_SUCCESS + reportMember.memberId)

            return ReportStateResponse.reportSuccess()
        }
    }
