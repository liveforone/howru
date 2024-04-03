package howru.howru.reportState.service.query

import howru.howru.reportState.repository.ReportStateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class ReportStateQueryService
    @Autowired
    constructor(
        private val reportStateRepository: ReportStateRepository
    ) {
        fun getOneByMemberId(memberId: UUID) = reportStateRepository.findReportStateInfoByMemberId(memberId)
    }
