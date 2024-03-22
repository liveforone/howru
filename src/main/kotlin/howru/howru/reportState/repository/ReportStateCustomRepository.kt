package howru.howru.reportState.repository

import howru.howru.reportState.domain.ReportState
import howru.howru.reportState.dto.response.ReportStateInfo
import java.util.*

interface ReportStateCustomRepository {
    fun findReportStateByMemberEmail(email: String): ReportState
    fun findReportStateByMemberId(memberId: UUID): ReportState
    fun findReportStateInfoByMemberId(memberId: UUID): ReportStateInfo
}