package howru.howru.comments.repository

import howru.howru.comments.domain.Comments
import howru.howru.comments.dto.response.CommentsInfo
import java.util.UUID

interface CommentsCustomRepository {
    fun findOneById(id: Long): Comments
    fun findOneByIdAndWriter(id: Long, writerId: UUID): Comments
    fun findOneDtoById(id: Long): CommentsInfo
    fun findCommentsByWriter(writerId: UUID, lastId: Long?): List<CommentsInfo>
    fun findCommentsByPost(postId: Long, lastId: Long?): List<CommentsInfo>
}