package howru.howru.exception.exception

import howru.howru.exception.message.RepostStateExceptionMessage

class ReportStateException(
    val repostStateExceptionMessage: RepostStateExceptionMessage,
    val memberIdentifier: String
) : RuntimeException(repostStateExceptionMessage.message)
