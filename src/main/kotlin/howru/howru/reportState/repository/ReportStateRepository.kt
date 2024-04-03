package howru.howru.reportState.repository

import howru.howru.reportState.domain.ReportState
import org.springframework.data.jpa.repository.JpaRepository

interface ReportStateRepository : JpaRepository<ReportState, Long>, ReportStateCustomRepository
