package howru.howru.advertisement.dto.request

import jakarta.validation.constraints.NotBlank

data class UpdateAdvertisement(
    @field:NotBlank(message = "변경할 광고 제목을 입력하세요.") var title: String?,
    @field:NotBlank(message = "변경할 광고 내용을 입력하세요.") var content: String?
)
