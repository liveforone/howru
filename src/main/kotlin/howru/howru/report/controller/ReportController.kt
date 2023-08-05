package howru.howru.report.controller

import howru.howru.globalUtil.validateBinding
import howru.howru.report.dto.ReportMember
import howru.howru.report.handler.ReportHandler
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ReportController @Autowired constructor(
    private val reportHandler: ReportHandler
) {
    @PostMapping(ReportUrl.REPORT)
    fun reportMember(
        @RequestBody @Valid reportMember: ReportMember,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        validateBinding(bindingResult)
        reportHandler.reportMember(reportMember)
        return ReportResponse.reportSuccess()
    }
}