package howru.howru.exception.exception

import howru.howru.exception.message.MemberExceptionMessage

class MemberException(val memberExceptionMessage: MemberExceptionMessage) : RuntimeException(memberExceptionMessage.message)