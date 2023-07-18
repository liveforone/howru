package howru.howru.exception.exception

import howru.howru.exception.message.SubscribeExceptionMessage

class SubscribeException(val subscribeExceptionMessage: SubscribeExceptionMessage) : RuntimeException(subscribeExceptionMessage.message)