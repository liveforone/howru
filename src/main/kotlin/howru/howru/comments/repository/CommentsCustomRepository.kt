package howru.howru.comments.repository

import howru.howru.comments.domain.Comments
import howru.howru.comments.dto.response.CommentsInfo
import howru.howru.comments.dto.response.CommentsPage
import java.util.*

interface CommentsCustomRepository {
    fun findCommentById(id: Long): Comments

    fun findCommentByIdAndWriter(
        id: Long,
        writerId: UUID
    ): Comments

    fun findCommentsInfoById(id: Long): CommentsInfo

    fun findCommentsByMember(
        memberId: UUID,
        lastId: Long?
    ): CommentsPage

    fun findCommentsByPost(
        postId: Long,
        lastId: Long?
    ): CommentsPage
}
