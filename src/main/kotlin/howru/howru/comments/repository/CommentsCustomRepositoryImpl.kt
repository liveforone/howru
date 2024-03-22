package howru.howru.comments.repository

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import howru.howru.comments.domain.Comments
import howru.howru.comments.domain.QComments
import howru.howru.comments.dto.response.CommentsInfo
import howru.howru.comments.repository.constant.CommentsRepoConstant
import howru.howru.exception.exception.CommentsException
import howru.howru.exception.message.CommentsExceptionMessage
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

    override fun findCommentByIdAndWriter(id: Long, writerId: UUID): Comments {
        return jpaQueryFactory.selectFrom(comments)
            .where(comments.id.eq(id).and(comments.writer.id.eq(writerId)))
            .fetchOne() ?: throw CommentsException(CommentsExceptionMessage.COMMENTS_IS_NULL, id)
    }

    override fun findCommentsInfoById(id: Long): CommentsInfo {
        return jpaQueryFactory.select(
            Projections.constructor(
                CommentsInfo::class.java,
                comments.id,
                comments.writer.id,
                comments.post.id,
                comments.content,
                comments.commentsState,
                comments.createdDatetime
            )
        )
            .from(comments)
            .where(comments.id.eq(id))
            .fetchOne() ?: throw CommentsException(CommentsExceptionMessage.COMMENTS_IS_NULL, id)
    }

    override fun findCommentsByWriter(writerId: UUID, lastId: Long?): List<CommentsInfo> {
        return jpaQueryFactory.select(
            Projections.constructor(
                CommentsInfo::class.java,
                comments.id,
                comments.writer.id,
                comments.post.id,
                comments.content,
                comments.commentsState,
                comments.createdDatetime
            )
        )
            .from(comments)
            .where(comments.writer.id.eq(writerId).and(ltLastId(lastId)))
            .orderBy(comments.id.desc())
            .limit(CommentsRepoConstant.LIMIT_PAGE)
            .fetch()
    }

    override fun findCommentsByPost(postId: Long, lastId: Long?): List<CommentsInfo> {
        return jpaQueryFactory.select(
            Projections.constructor(
                CommentsInfo::class.java,
                comments.id,
                comments.writer.id,
                comments.post.id,
                comments.content,
                comments.commentsState,
                comments.createdDatetime
            )
        )
            .from(comments)
            .where(comments.post.id.eq(postId).and(ltLastId(lastId)))
            .orderBy(comments.id.desc())
            .limit(CommentsRepoConstant.LIMIT_PAGE)
            .fetch()
    }

    private fun ltLastId(lastId: Long?): BooleanExpression? =
        lastId?.takeIf { it > 0 }?.let { comments.id.lt(it) }
}