package howru.howru.exception.exception

import howru.howru.exception.message.PostExceptionMessage

class PostException(val postExceptionMessage: PostExceptionMessage) : RuntimeException(postExceptionMessage.message)