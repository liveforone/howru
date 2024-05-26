package howru.howru.comments.controller.constant

object CommentsUrl {
    const val DETAIL = "/comments/{id}"
    const val MY_COMMENTS = "/comments/my"
    const val CREATE_COMMENTS = "/comments"
    const val EDIT_COMMENTS = "/comments/{id}"
    const val REMOVE_COMMENTS = "/comments/{id}"
    const val REPLY_PAGE = "/comments/{id}/replies"
}

object CommentsParam {
    const val ID = "id"
    const val LAST_ID = "lastId"
}
