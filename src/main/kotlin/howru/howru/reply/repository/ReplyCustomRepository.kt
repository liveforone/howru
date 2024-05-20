package howru.howru.reply.repository

import howru.howru.reply.domain.Reply
import howru.howru.reply.domain.vo.ReplyInfo
import howru.howru.reply.domain.vo.ReplyPage
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
    ): ReplyPage

    fun findRepliesByComment(
        commentId: Long,
        lastId: Long?
    ): ReplyPage
}
