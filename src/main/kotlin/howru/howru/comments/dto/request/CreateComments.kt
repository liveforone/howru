package howru.howru.comments.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class CreateComments(
    @field:NotNull(message = "작성자의 외부식별자를 입력하세요.")
    val writerUUID: UUID?,
    @field:NotNull(message = "게시글의 외부식별자를 입력하세요.")
    val postUUID: UUID?,
    @field:NotBlank(message = "댓글을 입력하세요.")
    val content: String?
)