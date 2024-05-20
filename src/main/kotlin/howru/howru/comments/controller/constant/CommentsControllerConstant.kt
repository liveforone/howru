package howru.howru.comments.controller.constant

object CommentsUrl {
    const val DETAIL = "/comments/{id}"
    const val COMMENTS_BY_WRITER = "/comments/writer/{writerId}"
    const val COMMENTS_BY_POST = "/comments/post/{postId}"
    const val COMMENTS_BY_SOMEONE = "/comments/someone/{writerId}"
    const val CREATE_COMMENTS = "/comments/create"
    const val EDIT_COMMENTS = "/comments/{id}/edit"
    const val REMOVE_COMMENTS = "/comments/{id}/remove"
}

object CommentsParam {
    const val ID = "id"
    const val WRITER_ID = "writerId"
    const val POST_ID = "postId"
    const val MEMBER_ID = "memberId"
    const val LAST_ID = "lastId"
}
