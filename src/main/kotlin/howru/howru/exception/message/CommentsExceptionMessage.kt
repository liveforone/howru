package howru.howru.exception.message

enum class CommentsExceptionMessage(val status: Int, val message: String) {
    COMMENTS_IS_NULL(404, "댓글이 존재하지 않습니다."),
    IS_NOT_FOLLOW_EACH(400, "맞팔로우하지 않은 회원의 댓글을 확인하는 것은 불가능합니다.")
}