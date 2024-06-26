package howru.howru.subscribe.dto.request

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class CreateSubscribe(
    @field:NotNull(message = "팔로잉 대상의 식별자를 입력하세요.") var followeeId: UUID?,
    @field:NotNull(message = "팔로워의 식별자를 입력하세요.") var followerId: UUID?
)
