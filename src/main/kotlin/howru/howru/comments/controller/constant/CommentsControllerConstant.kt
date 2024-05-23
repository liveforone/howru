package howru.howru.comments.controller.constant

object CommentsUrl {
    const val DETAIL = "/comments/{id}"
    const val COMMENTS_PAGE = "/comments"
    const val CREATE_COMMENTS = "/comments/create"
    const val EDIT_COMMENTS = "/comments/{id}"
    const val REMOVE_COMMENTS = "/comments/{id}"
}

object CommentsParam {
    const val ID = "id"
    const val WRITER_ID = "writerId"
    const val POST_ID = "postId"
    const val MEMBER_ID = "memberId"
    const val LAST_ID = "lastId"
}
