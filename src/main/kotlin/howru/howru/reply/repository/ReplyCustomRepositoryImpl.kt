package howru.howru.reply.repository

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import howru.howru.reply.exception.ReplyException
import howru.howru.reply.exception.ReplyExceptionMessage
import howru.howru.global.util.findLastIdOrDefault
import howru.howru.global.util.ltLastId
import howru.howru.reply.domain.QReply
import howru.howru.reply.domain.Reply
import howru.howru.reply.dto.response.ReplyInfo
import howru.howru.reply.dto.response.ReplyPage
import howru.howru.reply.repository.constant.ReplyRepoConstant
import java.util.*

class ReplyCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val reply: QReply = QReply.reply
) : ReplyCustomRepository {
    override fun findReplyByIdAndWriter(
        id: Long,
        writerId: UUID
    ): Reply {
        return jpaQueryFactory.selectFrom(reply)
            .where(reply.id.eq(id).and(reply.writer.id.eq(writerId)))
            .fetchOne() ?: throw ReplyException(ReplyExceptionMessage.REPLY_IS_NULL, id)
    }

    private val replyInfoField =
        Projections.constructor(
            ReplyInfo::class.java,
            reply.id,
            reply.writer.id,
            reply.comment.id,
            reply.content,
            reply.replyState,
            reply.createdDatetime
        )

    override fun findReplyInfoById(id: Long): ReplyInfo {
        return jpaQueryFactory.select(replyInfoField)
            .from(reply)
            .where(reply.id.eq(id))
            .fetchOne() ?: throw ReplyException(ReplyExceptionMessage.REPLY_IS_NULL, id)
    }

    override fun findRepliesByWriter(
        writerId: UUID,
        lastId: Long?
    ): ReplyPage {
        val replyInfoList =
            jpaQueryFactory.select(replyInfoField)
                .from(reply)
                .where(reply.writer.id.eq(writerId).and(ltLastId(lastId, reply) { it.id }))
                .orderBy(reply.id.desc())
                .limit(ReplyRepoConstant.LIMIT_PAGE)
                .fetch()

        return ReplyPage(replyInfoList, findLastIdOrDefault(replyInfoList) { it.id })
    }

    override fun findRepliesByComment(
        commentId: Long,
        lastId: Long?
    ): ReplyPage {
        val replyInfoList =
            jpaQueryFactory.select(replyInfoField)
                .from(reply)
                .where(reply.comment.id.eq(commentId).and(ltLastId(lastId, reply) { it.id }))
                .orderBy(reply.id.desc())
                .limit(ReplyRepoConstant.LIMIT_PAGE)
                .fetch()

        return ReplyPage(replyInfoList, findLastIdOrDefault(replyInfoList) { it.id })
    }
}
