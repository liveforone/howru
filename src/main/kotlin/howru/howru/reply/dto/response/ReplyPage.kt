package howru.howru.reply.dto.response

data class ReplyPage(
    val replyInfoList: List<ReplyInfo>,
    val lastId: Long
)
