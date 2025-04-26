package howru.howru.likes.exception

class LikesException(
    val likesExceptionMessage: LikesExceptionMessage,
    val postId: Long
) : RuntimeException(
        likesExceptionMessage.message
    )
