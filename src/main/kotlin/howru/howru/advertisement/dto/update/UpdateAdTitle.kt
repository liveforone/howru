package howru.howru.advertisement.dto.update

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class UpdateAdTitle(
    @field:NotNull(message = "광고의 식별자를 입력하세요.") val id: Long?,
    @field:NotBlank(message = "변경할 광고 제목을 입력하세요.") val title: String?
)
