package howru.howru.exception.exception

import howru.howru.exception.message.CommentsExceptionMessage

class CommentsException(val commentsExceptionMessage: CommentsExceptionMessage, val commentId: Long) : RuntimeException(
    commentsExceptionMessage.message
)
