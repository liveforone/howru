package howru.howru.reply.repository

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import howru.howru.exception.exception.ReplyException
import howru.howru.exception.message.ReplyExceptionMessage
import howru.howru.reply.domain.QReply
import howru.howru.reply.domain.Reply
import howru.howru.reply.dto.response.ReplyInfo
import howru.howru.reply.repository.constant.ReplyRepoConstant
import java.util.*

class ReplyCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val reply: QReply = QReply.reply
) : ReplyCustomRepository {
    override fun findReplyByIdAndWriter(id: Long, writerId: UUID): Reply {
        return jpaQueryFactory.selectFrom(reply)
            .where(reply.id.eq(id).and(reply.writer.id.eq(writerId)))
            .fetchOne() ?: throw ReplyException(ReplyExceptionMessage.REPLY_IS_NULL, id)
    }

    override fun findReplyInfoById(id: Long): ReplyInfo {
        return jpaQueryFactory.select(
            Projections.constructor(
                ReplyInfo::class.java,
                reply.id,
                reply.writer.id,
                reply.comment.id,
                reply.content,
                reply.replyState,
                reply.createdDatetime
            )
        )
            .from(reply)
            .where(reply.id.eq(id))
            .fetchOne() ?: throw ReplyException(ReplyExceptionMessage.REPLY_IS_NULL, id)
    }

    override fun findRepliesByWriter(writerId: UUID, lastId: Long?): List<ReplyInfo> {
        return jpaQueryFactory.select(
            Projections.constructor(
                ReplyInfo::class.java,
                reply.id,
                reply.writer.id,
                reply.comment.id,
                reply.content,
                reply.replyState,
                reply.createdDatetime
            )
        )
            .from(reply)
            .where(reply.writer.id.eq(writerId).and(ltLastId(lastId)))
            .orderBy(reply.id.desc())
            .limit(ReplyRepoConstant.LIMIT_PAGE)
            .fetch()
    }

    override fun findRepliesByComment(commentId: Long, lastId: Long?): List<ReplyInfo> {
        return jpaQueryFactory.select(
            Projections.constructor(
                ReplyInfo::class.java,
                reply.id,
                reply.writer.id,
                reply.comment.id,
                reply.content,
                reply.replyState,
                reply.createdDatetime
            )
        )
            .from(reply)
            .where(reply.comment.id.eq(commentId).and(ltLastId(lastId)))
            .orderBy(reply.id.desc())
            .limit(ReplyRepoConstant.LIMIT_PAGE)
            .fetch()
    }

    private fun ltLastId(lastId: Long?): BooleanExpression? =
        lastId?.takeIf { it > 0 }?.let { reply.id.lt(it) }
}