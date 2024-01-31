package howru.howru.subscribe.domain

import java.io.Serializable
import java.util.UUID

data class SubscribePk(val followeeId: UUID? = null, val followerId: UUID? = null) : Serializable