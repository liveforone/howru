package howru.howru.advertisement.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class UpdateAdContent(
    @field:NotNull(message = "광고의 식별자를 입력하세요.") val id: Long?,
    @field:NotBlank(message = "변경할 광고 내용을 입력하세요.") val content: String?
)
