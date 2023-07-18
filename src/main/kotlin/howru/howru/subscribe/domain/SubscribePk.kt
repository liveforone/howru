package howru.howru.subscribe.domain

import java.io.Serializable
import java.util.UUID

data class SubscribePk(
    val followeeUUID: UUID? = null,
    val followerUUID: UUID? = null
) : Serializable