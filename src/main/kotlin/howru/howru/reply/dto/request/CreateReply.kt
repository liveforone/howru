package howru.howru.reply.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class CreateReply(
    @field:NotNull(message = "작성자의 외부식별자를 입력하세요.")
    val writerUUID: UUID?,
    @field:NotNull(message = "댓글의 외부식별자를 입력하세요.")
    val commentUUID: UUID?,
    @field:NotBlank(message = "대댓글을 입력하세요.")
    val content: String?
)
