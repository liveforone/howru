package howru.howru.reportState.dto.request

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class ReportMember(@field:NotNull(message = "신고할 회원의 식별자를 입력하세요.") val memberId: UUID?)
