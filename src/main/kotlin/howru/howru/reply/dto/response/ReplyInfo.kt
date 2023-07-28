package howru.howru.reply.dto.response

import howru.howru.reply.domain.ReplyState
import java.util.UUID

data class ReplyInfo(val uuid: UUID, val writerUUID: UUID, val commentUUID: UUID, val content: String, val replyState: ReplyState, val createdDate: Long)