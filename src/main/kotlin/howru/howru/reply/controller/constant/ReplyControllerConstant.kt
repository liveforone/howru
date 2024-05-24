package howru.howru.reply.controller.constant

object ReplyUrl {
    const val DETAIL = "/replies/{id}"
    const val REPLY_PAGE = "/replies"
    const val MY_REPLY = "/replies/my"
    const val CREATE = "/replies"
    const val EDIT = "/replies/{id}"
    const val REMOVE = "/replies/{id}"
}

object ReplyParam {
    const val ID = "id"
    const val LAST_ID = "lastId"
    const val COMMENT_ID = "commentId"
}
