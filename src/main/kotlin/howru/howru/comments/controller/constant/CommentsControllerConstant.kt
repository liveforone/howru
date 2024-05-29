package howru.howru.comments.controller.constant

object CommentsUrl {
    const val DETAIL = "/comments/{id}"
    const val COMMENTS_PAGE = "/comments"
    const val COMMENTS_OF_OTHER_MEMBER = "/comments"
    const val MY_COMMENTS = "/comments/my"
    const val CREATE_COMMENTS = "/comments"
    const val EDIT_COMMENTS = "/comments/{id}"
    const val REMOVE_COMMENTS = "/comments/{id}"
}

object CommentsParam {
    const val ID = "id"
    const val POST_ID = "post-id"
    const val MEMBER_ID = "member-id"
    const val LAST_ID = "last-id"
}
