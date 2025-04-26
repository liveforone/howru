package howru.howru.reply.exception

class ReplyException(
    val replyExceptionMessage: ReplyExceptionMessage,
    val replyId: Long
) : RuntimeException(
        replyExceptionMessage.message
    )
