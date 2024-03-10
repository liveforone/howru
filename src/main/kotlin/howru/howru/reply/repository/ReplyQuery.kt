package howru.howru.reply.repository

import howru.howru.comments.domain.Comments
import howru.howru.exception.exception.ReplyException
import howru.howru.exception.message.ReplyExceptionMessage
import howru.howru.member.domain.Member
import howru.howru.reply.domain.Reply
import howru.howru.reply.dto.response.ReplyInfo
import howru.howru.reply.repository.constant.ReplyRepoConstant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.util.*

@Component
class ReplyQuery @Autowired constructor(
    private val replyRepository: ReplyRepository
) {
    fun findOneByIdAndWriter(id: Long, writerId: UUID): Reply {
        return replyRepository.findAll {
            select(entity(Reply::class))
                .from(entity(Reply::class), join(Reply::writer))
                .where(path(Reply::id).eq(id).and(path(Member::id).eq(writerId)))
        }.firstOrNull() ?: throw ReplyException(ReplyExceptionMessage.REPLY_IS_NULL, id)
    }

    fun findOneDtoById(id: Long): ReplyInfo {
        return replyRepository.findAll {
            selectNew<ReplyInfo>(
                path(Reply::id),
                path(Member::id),
                path(Comments::id),
                path(Reply::content),
                path(Reply::replyState),
                path(Reply::createdDatetime)
            ).from(entity(Reply::class), join(Reply::writer), join(Reply::comment))
                .where(path(Reply::id).eq(id))
        }.firstOrNull() ?: throw ReplyException(ReplyExceptionMessage.REPLY_IS_NULL, id)
    }

    fun findRepliesByWriter(writerId: UUID, page: Int): List<ReplyInfo> {
        val pageable = PageRequest.of(page, ReplyRepoConstant.LIMIT_PAGE)
        return replyRepository.findAll(pageable) {
            selectNew<ReplyInfo>(
                path(Reply::id),
                path(Member::id),
                path(Comments::id),
                path(Reply::content),
                path(Reply::replyState),
                path(Reply::createdDatetime)
            ).from(entity(Reply::class), join(Reply::writer), join(Reply::comment))
                .where(path(Member::id).eq(writerId))
                .orderBy(path(Reply::id).desc())
        }.filterNotNull()
    }

    fun findRepliesByComment(commentId: Long, page: Int): List<ReplyInfo> {
        val pageable = PageRequest.of(page, ReplyRepoConstant.LIMIT_PAGE)
        return replyRepository.findAll(pageable) {
            selectNew<ReplyInfo>(
                path(Reply::id),
                path(Member::id),
                path(Comments::id),
                path(Reply::content),
                path(Reply::replyState),
                path(Reply::createdDatetime)
            ).from(entity(Reply::class), join(Reply::writer), join(Reply::comment))
                .where(path(Comments::id).eq(commentId))
                .orderBy(path(Reply::id).desc())
        }.filterNotNull()
    }
}