package howru.howru.likes.dto.request

import jakarta.validation.constraints.NotNull
import java.util.*

data class DeleteLikes(
    @field:NotNull(message = "회원의 외부 식별자를 입력하세요.")
    val memberUUID: UUID?,
    @field:NotNull(message = "게시글의 외부 식별자를 입력하세요.")
    val postUUID: UUID?
)