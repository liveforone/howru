package howru.howru.reportState.dto.response

import howru.howru.reportState.domain.MemberState

data class RepostStateInfo(val memberState: MemberState, val modifiedStateDate: Int, val reportCount: Int)