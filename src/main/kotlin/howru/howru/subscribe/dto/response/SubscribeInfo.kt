package howru.howru.subscribe.dto.response

import java.util.UUID

data class SubscribeInfo(
    val followeeUUID: UUID,
    val followerUUID: UUID
)
