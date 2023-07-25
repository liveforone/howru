package howru.howru.comments.dto.update

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class UpdateCommentsContent(
    @field:NotNull(message = "댓글의 외부식별자를 입력하세요.")
    val uuid: UUID?,
    @field:NotNull(message = "작성자의 외부식별자를 입력하세요.")
    val writerUUID: UUID?,
    @field:NotBlank(message = "변경할 댓글을 입력하세요.")
    val content: String?
)
