package howru.howru.comments.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.util.UUID

data class CreateComments(
    @field:NotNull(message = "작성자의 식별자를 입력하세요.") var writerId: UUID?,
    @field:NotNull(message = "게시글의 식별자를 입력하세요.") var postId: Long?,
    @field:NotBlank(message = "댓글을 입력하세요.") @field:Size(max = 100, message = "댓글 제한 길이를 초과하였습니다.") var content: String?
)
