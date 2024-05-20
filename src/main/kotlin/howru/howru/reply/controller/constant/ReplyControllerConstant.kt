package howru.howru.reply.controller.constant

object ReplyUrl {
    const val DETAIL = "/reply/{id}"
    const val BELONG_WRITER = "/reply/belong/writer/{writerId}"
    const val BELONG_COMMENT = "/reply/belong/comment/{commentId}"
    const val CREATE = "/reply/create"
    const val EDIT = "/reply/{id}/edit"
    const val REMOVE = "/reply/{id}/remove"
}

object ReplyParam {
    const val ID = "id"
    const val LAST_ID = "lastId"
    const val WRITER_ID = "writerId"
    const val COMMENT_ID = "commentId"
}
