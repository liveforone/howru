package howru.howru.comments.repository

import howru.howru.comments.domain.Comments
import howru.howru.comments.dto.response.CommentsInfo
import java.util.UUID

interface CommentsCustomRepository {
    fun findOneByUUID(uuid: UUID): Comments
    fun findOneByUUIDAndWriter(uuid: UUID, writerUUID: UUID): Comments
    fun findOneDtoByUUID(uuid: UUID): CommentsInfo
    fun findCommentsByWriter(writerUUID: UUID, lastUUID: UUID?): List<CommentsInfo>
    fun findCommentsByPost(postUUID: UUID, lastUUID: UUID?): List<CommentsInfo>
}