package howru.howru.exception.exception

import howru.howru.exception.message.ReplyExceptionMessage

class ReplyException(val replyExceptionMessage: ReplyExceptionMessage, val replyId: Long) : RuntimeException(
    replyExceptionMessage.message
)
