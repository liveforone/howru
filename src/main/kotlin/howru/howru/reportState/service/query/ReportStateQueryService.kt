package howru.howru.reportState.service.query

import howru.howru.reportState.repository.ReportStateQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class ReportStateQueryService @Autowired constructor(
    private val reportStateQuery: ReportStateQuery
) {
    fun getOneByMemberId(memberId: UUID) = reportStateQuery.findOneDtoByMemberId(memberId)
}