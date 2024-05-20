package howru.howru.reply.dto

import jakarta.validation.constraints.NotNull
import java.util.*

data class RemoveReply(
    @field:NotNull(message = "작성자의 식별자를 입력하세요.") var writerId: UUID?
)
