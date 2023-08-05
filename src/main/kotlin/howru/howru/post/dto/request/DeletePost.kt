package howru.howru.post.dto.request

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class DeletePost(
    @field:NotNull(message = "게시글 외부식별자를 입력하세요.") val uuid: UUID?,
    @field:NotNull(message = "작성자의 외부식별자를 입력하세요.") val writerUUID: UUID?
)