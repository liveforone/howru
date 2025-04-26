package howru.howru.reply.exception

enum class ReplyExceptionMessage(
    val status: Int,
    val message: String
) {
    REPLY_IS_NULL(404, "대댓글이 존재하지 않습니다. 대댓글 ID : ")
}
