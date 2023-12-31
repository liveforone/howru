package howru.howru.reply.repository

import howru.howru.reply.domain.Reply
import howru.howru.reply.dto.response.ReplyInfo
import java.util.UUID

interface ReplyCustomRepository {
    fun findOneByIdAndWriter(id: Long, writerUUID: UUID): Reply
    fun findOneDtoById(id: Long): ReplyInfo
    fun findRepliesByWriter(writerUUID: UUID, lastId: Long?): List<ReplyInfo>
    fun findRepliesByComment(commentId: Long, lastId: Long?): List<ReplyInfo>
}