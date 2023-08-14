package howru.howru.reportState.repository

import howru.howru.reportState.domain.ReportState
import howru.howru.reportState.dto.response.RepostStateInfo
import java.util.UUID

interface RepostStateCustomRepository {
    fun findOneByMemberEmail(email: String): ReportState
    fun findOneByMemberUUID(memberUUID: UUID): ReportState
    fun findOneDtoByMemberUUID(memberUUID: UUID): RepostStateInfo
}