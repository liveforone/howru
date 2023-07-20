package howru.howru.post.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.util.UUID

data class CreatePost(
    @field:NotNull(message = "작성자의 외부 식별자를 입력하세요.")
    val writerUUID: UUID?,
    @field:NotBlank(message = "게시글을 입력하세요.")
    @field:Size(max = 800)
    val content: String?
)
