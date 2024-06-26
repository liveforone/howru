package howru.howru.reply.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.util.*

data class UpdateReplyContent(
    @field:NotNull(message = "작성자의 식별자를 입력하세요.") var writerId: UUID?,
    @field:NotBlank(message = "변경할 대댓글을 입력하세요.") @field:Size(max = 100, message = "대댓글 제한 길이를 초과하였습니다.")
    var content: String?
)
