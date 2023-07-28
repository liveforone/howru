package howru.howru.reply.dto.update

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.*

data class UpdateReplyContent(
    @field:NotNull(message = "대댓글의 외부식별자를 입력하세요.")
    val uuid: UUID?,
    @field:NotNull(message = "작성자의 외부식별자를 입력하세요.")
    val writerUUID: UUID?,
    @field:NotBlank(message = "변경할 대댓글을 입력하세요.")
    val content: String?
)
