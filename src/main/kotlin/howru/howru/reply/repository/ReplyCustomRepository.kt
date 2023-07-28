package howru.howru.reply.repository

import howru.howru.reply.domain.Reply
import howru.howru.reply.dto.response.ReplyInfo
import java.util.UUID

interface ReplyCustomRepository {
    fun findOneByUUIDAndWriter(uuid: UUID, writerUUID: UUID): Reply
    fun findOneDtoByUUID(uuid: UUID): ReplyInfo
    fun findRepliesByWriter(writerUUID: UUID, lastUUID: UUID?): List<ReplyInfo>
    fun findRepliesByComment(commentUUID: UUID, lastUUID: UUID?): List<ReplyInfo>
}