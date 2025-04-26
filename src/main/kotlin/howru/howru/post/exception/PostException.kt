package howru.howru.post.exception

class PostException(
    val postExceptionMessage: PostExceptionMessage,
    val postId: Long
) : RuntimeException(
        postExceptionMessage.message
    )
