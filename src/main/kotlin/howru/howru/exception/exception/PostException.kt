package howru.howru.exception.exception

import howru.howru.exception.message.PostExceptionMessage

class PostException(val postExceptionMessage: PostExceptionMessage, val postId: Long) : RuntimeException(
    postExceptionMessage.message
)
