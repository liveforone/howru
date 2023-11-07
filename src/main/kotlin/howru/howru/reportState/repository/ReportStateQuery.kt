package howru.howru.reportState.repository

import howru.howru.exception.exception.ReportStateException
import howru.howru.exception.message.RepostStateExceptionMessage
import howru.howru.member.domain.Member
import howru.howru.reportState.domain.ReportState
import howru.howru.reportState.dto.response.ReportStateInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ReportStateQuery @Autowired constructor(
    private val reportStateRepository: ReportStateRepository
) {
    fun findOneByMemberEmail(email: String): ReportState {
        return reportStateRepository.findAll {
            select(entity(ReportState::class))
                .from(entity(ReportState::class), join(ReportState::member))
                .where(path(Member::email).eq(email))
        }.firstOrNull() ?: throw ReportStateException(RepostStateExceptionMessage.REPORT_STATE_IS_NULL, email)
    }

    fun findOneByMemberUUID(memberUUID: UUID): ReportState {
        return reportStateRepository.findAll {
            select(entity(ReportState::class))
                .from(entity(ReportState::class), join(ReportState::member))
                .where(path(Member::uuid).eq(memberUUID))
        }.firstOrNull() ?: throw ReportStateException(RepostStateExceptionMessage.REPORT_STATE_IS_NULL, memberUUID.toString())
    }

    fun findOneDtoByMemberUUID(memberUUID: UUID): ReportStateInfo {
        return reportStateRepository.findAll {
            selectNew<ReportStateInfo>(
                path(ReportState::memberState),
                path(ReportState::modifiedStateDate),
                path(ReportState::reportCount)
            ).from(entity(ReportState::class), join(ReportState::member))
                .where(path(Member::uuid).eq(memberUUID))
        }.firstOrNull() ?: throw ReportStateException(RepostStateExceptionMessage.REPORT_STATE_IS_NULL, memberUUID.toString())
    }
}