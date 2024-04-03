package howru.howru.advertisement.dto.request

import jakarta.validation.constraints.NotBlank

data class UpdateAdTitle(
    @field:NotBlank(message = "변경할 광고 제목을 입력하세요.") val title: String?
)
