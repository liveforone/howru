package howru.howru.advertisement.dto.request

import jakarta.validation.constraints.NotBlank

data class UpdateAdContent(@field:NotBlank(message = "변경할 광고 내용을 입력하세요.") val content: String?)
