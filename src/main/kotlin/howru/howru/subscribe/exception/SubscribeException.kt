package howru.howru.subscribe.exception

import java.util.UUID

class SubscribeException(
    val subscribeExceptionMessage: SubscribeExceptionMessage,
    val followerUUID: UUID
) : RuntimeException(subscribeExceptionMessage.message)
