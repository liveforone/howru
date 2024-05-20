package howru.howru.reply.domain.vo

import howru.howru.reply.domain.ReplyState
import java.util.UUID

data class ReplyInfo(
    val id: Long,
    val writerId: UUID,
    val commentId: Long,
    val content: String,
    val replyState: ReplyState,
    val createdDatetime: Long
)
