package howru.howru.report.dto

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class ReportMember(@field:NotNull(message = "신고할 회원의 외부식별자를 입력하세요.") val memberUUID: UUID?)
