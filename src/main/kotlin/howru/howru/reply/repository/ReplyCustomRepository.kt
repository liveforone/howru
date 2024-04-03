package howru.howru.reply.repository

import howru.howru.reply.domain.Reply
import howru.howru.reply.dto.response.ReplyInfo
import java.util.*

interface ReplyCustomRepository {
    fun findReplyByIdAndWriter(
        id: Long,
        writerId: UUID
    ): Reply

    fun findReplyInfoById(id: Long): ReplyInfo

    fun findRepliesByWriter(
        writerId: UUID,
        lastId: Long?
    ): List<ReplyInfo>

    fun findRepliesByComment(
        commentId: Long,
        lastId: Long?
    ): List<ReplyInfo>
}
