package howru.howru.post.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.util.UUID

data class UpdatePostContent(
    @field:NotNull(message = "작성자의 식별자를 입력하세요.") val writerId: UUID?,
    @field:NotBlank(message = "게시글을 입력하세요.") @field:Size(max = 800, message = "게시글 제한 길이를 초과하였습니다.")
    val content: String?
)
