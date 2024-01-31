package howru.howru.subscribe.dto.request

import jakarta.validation.constraints.NotNull
import java.util.*

data class UnsubscribeRequest(
    @field:NotNull(message = "팔로잉 대상의 식별자를 입력하세요.") val followeeId: UUID?,
    @field:NotNull(message = "팔로워의 식별자를 입력하세요.") val followerId: UUID?
)
