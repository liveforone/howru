package howru.howru.likes.dto.request

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class CreateLikes(
    @field:NotNull(message = "회원의 식별자를 입력하세요.") val memberId: UUID?,
    @field:NotNull(message = "게시글의 식별자를 입력하세요.") val postId: Long?
)
