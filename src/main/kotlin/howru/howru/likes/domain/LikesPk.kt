package howru.howru.likes.domain

import java.io.Serializable
import java.util.*

data class LikesPk(
    val memberId: UUID? = null,
    val postId: Long? = null
) : Serializable
