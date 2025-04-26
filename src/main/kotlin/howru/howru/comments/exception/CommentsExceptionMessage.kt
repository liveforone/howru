package howru.howru.comments.exception

enum class CommentsExceptionMessage(
    val status: Int,
    val message: String
) {
    COMMENTS_IS_NULL(404, "댓글이 존재하지 않습니다. 댓글 ID : ")
}
