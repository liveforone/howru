package howru.howru.comments.dto.request

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class DeleteComments(
    @field:NotNull(message = "댓글의 식별자를 입력하세요.") val id: Long?,
    @field:NotNull(message = "작성자의 외부식별자를 입력하세요.") val writerUUID: UUID?
)
