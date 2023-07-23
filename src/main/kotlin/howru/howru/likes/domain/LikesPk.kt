package howru.howru.likes.domain

import java.io.Serializable
import java.util.*

data class LikesPk(
    val memberUUID: UUID? = null,
    val postUUID: UUID? = null
) : Serializable