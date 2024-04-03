package howru.howru.exception.exception

import howru.howru.exception.message.SubscribeExceptionMessage
import java.util.UUID

class SubscribeException(
    val subscribeExceptionMessage: SubscribeExceptionMessage,
    val followerUUID: UUID
) : RuntimeException(subscribeExceptionMessage.message)
