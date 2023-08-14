package howru.howru.reportState.controller.response

import howru.howru.reportState.dto.response.RepostStateInfo
import org.springframework.http.ResponseEntity

object ReportStateResponse {
    private const val REPORT_SUCCESS = "회원을 성공적으로 신고하였습니다."

    fun infoSuccess(reportState: RepostStateInfo) = ResponseEntity.ok(reportState)
    fun reportSuccess() = ResponseEntity.ok(REPORT_SUCCESS)
}