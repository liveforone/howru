package howru.howru.comments.exception

class CommentsException(val commentsExceptionMessage: CommentsExceptionMessage, val commentId: Long) : RuntimeException(
    commentsExceptionMessage.message
)
