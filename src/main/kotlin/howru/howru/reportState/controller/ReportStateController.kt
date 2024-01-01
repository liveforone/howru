package howru.howru.reportState.controller

import howru.howru.globalUtil.validateBinding
import howru.howru.logger
import howru.howru.reportState.controller.constant.ReportStateParam
import howru.howru.reportState.controller.constant.ReportStateUrl
import howru.howru.reportState.controller.response.ReportStateResponse
import howru.howru.reportState.dto.request.ReportMember
import howru.howru.reportState.dto.response.ReportStateInfo
import howru.howru.reportState.log.ReportStateControllerLog
import howru.howru.reportState.service.command.ReportStateCommandService
import howru.howru.reportState.service.query.ReportStateQueryService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class ReportStateController @Autowired constructor(
    private val reportStateQueryService: ReportStateQueryService,
    private val reportStateCommandService: ReportStateCommandService
) {
    @GetMapping(ReportStateUrl.REPORT_STATE_INFO)
    fun reportStateInfo(@PathVariable(ReportStateParam.MEMBER_UUID) memberUUID: UUID): ResponseEntity<ReportStateInfo> {
        val reportState = reportStateQueryService.getOneByMemberUUID(memberUUID)
        return ReportStateResponse.infoSuccess(reportState)
    }

    @PostMapping(ReportStateUrl.REPORT)
    fun reportMember(
        @RequestBody @Valid reportMember: ReportMember,
        bindingResult: BindingResult
    ): ResponseEntity<String> {
        validateBinding(bindingResult)

        reportStateCommandService.addRepost(reportMember)
        logger().info(ReportStateControllerLog.REPORT_MEMBER_SUCCESS + reportMember.memberUUID)

        return ReportStateResponse.reportSuccess()
    }
}