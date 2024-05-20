package howru.howru.reportState.domain.vo

import howru.howru.reportState.domain.MemberState

data class ReportStateInfo(val memberState: MemberState, val modifiedStateDate: Int, val reportCount: Int)
