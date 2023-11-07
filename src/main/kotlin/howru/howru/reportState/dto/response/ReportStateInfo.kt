package howru.howru.reportState.dto.response

import howru.howru.reportState.domain.MemberState

data class ReportStateInfo(val memberState: MemberState, val modifiedStateDate: Int, val reportCount: Int)