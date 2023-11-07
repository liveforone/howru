package howru.howru.reportState.repository

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import howru.howru.reportState.domain.ReportState
import org.springframework.data.jpa.repository.JpaRepository

interface ReportStateRepository : JpaRepository<ReportState, Long>, KotlinJdslJpqlExecutor