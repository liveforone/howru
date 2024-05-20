package howru.howru.member.exception

class MemberException(
    val memberExceptionMessage: MemberExceptionMessage,
    val memberIdentifier: String?
) : RuntimeException(memberExceptionMessage.message)
