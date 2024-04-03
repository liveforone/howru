package howru.howru.post.dto.request

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class RemovePost(
    @field:NotNull(message = "작성자의 식별자를 입력하세요.") val writerId: UUID?
)
