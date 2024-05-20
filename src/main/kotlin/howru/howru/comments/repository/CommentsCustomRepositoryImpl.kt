package howru.howru.comments.repository

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import howru.howru.comments.domain.Comments
import howru.howru.comments.domain.QComments
import howru.howru.comments.domain.vo.CommentsInfo
import howru.howru.comments.domain.vo.CommentsPage
import howru.howru.comments.repository.constant.CommentsRepoConstant
import howru.howru.comments.exception.CommentsException
import howru.howru.comments.exception.CommentsExceptionMessage
import howru.howru.global.util.findLastIdOrDefault
import howru.howru.global.util.ltLastId
import java.util.*

class CommentsCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val comments: QComments = QComments.comments
) : CommentsCustomRepository {
    override fun findCommentById(id: Long): Comments {
        return jpaQueryFactory.selectFrom(comments)
            .where(comments.id.eq(id))
            .fetchOne() ?: throw CommentsException(CommentsExceptionMessage.COMMENTS_IS_NULL, id)
    }

    override fun findCommentByIdAndWriter(
        id: Long,
        writerId: UUID
    ): Comments {
        return jpaQueryFactory.selectFrom(comments)
            .where(comments.id.eq(id).and(comments.writer.id.eq(writerId)))
            .fetchOne() ?: throw CommentsException(CommentsExceptionMessage.COMMENTS_IS_NULL, id)
    }

    private val commentsInfoField =
        Projections.constructor(
            CommentsInfo::class.java,
            comments.id,
            comments.writer.id,
            comments.post.id,
            comments.content,
            comments.commentsState,
            comments.createdDatetime
        )

    override fun findCommentsInfoById(id: Long): CommentsInfo {
        return jpaQueryFactory.select(commentsInfoField)
            .from(comments)
            .where(comments.id.eq(id))
            .fetchOne() ?: throw CommentsException(CommentsExceptionMessage.COMMENTS_IS_NULL, id)
    }

    override fun findCommentsByWriter(
        writerId: UUID,
        lastId: Long?
    ): CommentsPage {
        val commentsInfoList =
            jpaQueryFactory.select(commentsInfoField)
                .from(comments)
                .where(comments.writer.id.eq(writerId).and(ltLastId(lastId, comments) { it.id }))
                .orderBy(comments.id.desc())
                .limit(CommentsRepoConstant.LIMIT_PAGE)
                .fetch()

        return CommentsPage(commentsInfoList, findLastIdOrDefault(commentsInfoList) { it.id })
    }

    override fun findCommentsByPost(
        postId: Long,
        lastId: Long?
    ): CommentsPage {
        val commentsInfoList =
            jpaQueryFactory.select(commentsInfoField)
                .from(comments)
                .where(comments.post.id.eq(postId).and(ltLastId(lastId, comments) { it.id }))
                .orderBy(comments.id.desc())
                .limit(CommentsRepoConstant.LIMIT_PAGE)
                .fetch()

        return CommentsPage(commentsInfoList, findLastIdOrDefault(commentsInfoList) { it.id })
    }
}
