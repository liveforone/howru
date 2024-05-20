package howru.howru.reportState.exceptioin

class ReportStateException(
    val repostStateExceptionMessage: RepostStateExceptionMessage,
    val memberIdentifier: String
) : RuntimeException(repostStateExceptionMessage.message)
