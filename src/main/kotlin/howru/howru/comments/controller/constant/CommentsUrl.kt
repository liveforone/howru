package howru.howru.comments.controller.constant

object CommentsUrl {
    const val DETAIL = "/comments/{id}"
    const val COMMENTS_BY_WRITER = "/comments/writer/{writerUUID}"
    const val COMMENTS_BY_POST = "/comments/post/{postId}"
    const val COMMENTS_BY_SOMEONE = "/comments/someone/{writerUUID}"
    const val CREATE_COMMENTS = "/comments/create"
    const val EDIT_COMMENTS = "/comments/{id}/edit"
    const val REMOVE_COMMENTS = "/comments/{id}/remove"
}