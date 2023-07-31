package howru.howru.advertisement.dto.update

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.*

data class UpdateAdContent(
    @field:NotNull(message = "광고의 외부식별자를 입력하세요.")
    val uuid: UUID?,
    @field:NotBlank(message = "변경할 광고 내용을 입력하세요.")
    val content: String?
)
