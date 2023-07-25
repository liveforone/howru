package howru.howru.comments.controller.constant

object CommentsUrl {
    const val DETAIL = "/comments/detail/{uuid}"
    const val COMMENTS_BY_WRITER = "/comments/writer/{writerUUID}"
    const val COMMENTS_BY_POST = "/comments/post/{postUUID}"
    const val COMMENTS_BY_SOMEONE = "/comments/someone/{writerUUID}"
    const val CREATE = "/comments/create"
    const val EDIT = "/comments/edit"
    const val DELETE = "/comments/delete"
}