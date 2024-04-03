package howru.howru.exception.exception

import howru.howru.exception.message.LikesExceptionMessage

class LikesException(val likesExceptionMessage: LikesExceptionMessage, val postId: Long) : RuntimeException(
    likesExceptionMessage.message
)
