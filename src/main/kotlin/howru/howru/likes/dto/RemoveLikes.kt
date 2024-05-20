package howru.howru.likes.dto

import jakarta.validation.constraints.NotNull
import java.util.*

data class RemoveLikes(
    @field:NotNull(message = "회원의 식별자를 입력하세요.") var memberId: UUID?,
    @field:NotNull(message = "게시글의 식별자를 입력하세요.") var postId: Long?
)
