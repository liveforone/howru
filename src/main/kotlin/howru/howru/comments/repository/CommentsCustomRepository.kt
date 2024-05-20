package howru.howru.comments.repository

import howru.howru.comments.domain.Comments
import howru.howru.comments.domain.vo.CommentsInfo
import howru.howru.comments.domain.vo.CommentsPage
import java.util.*

interface CommentsCustomRepository {
    fun findCommentById(id: Long): Comments

    fun findCommentByIdAndWriter(
        id: Long,
        writerId: UUID
    ): Comments

    fun findCommentsInfoById(id: Long): CommentsInfo

    fun findCommentsByWriter(
        writerId: UUID,
        lastId: Long?
    ): CommentsPage

    fun findCommentsByPost(
        postId: Long,
        lastId: Long?
    ): CommentsPage
}
