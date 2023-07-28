package howru.howru.exception.message

enum class ReplyExceptionMessage(val status: Int, val message: String) {
    REPLY_IS_NULL(404, "대댓글이 존재하지 않습니다.")
}