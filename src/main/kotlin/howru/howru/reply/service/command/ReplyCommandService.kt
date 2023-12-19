package howru.howru.reply.service.command

import howru.howru.comments.repository.CommentsRepository
import howru.howru.member.repository.MemberQuery
import howru.howru.reply.domain.Reply
import howru.howru.reply.dto.request.CreateReply
import howru.howru.reply.dto.request.RemoveReply
import howru.howru.reply.dto.request.UpdateReplyContent
import howru.howru.reply.repository.ReplyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReplyCommandService @Autowired constructor(
    private val replyRepository: ReplyRepository,
    private val memberQuery: MemberQuery,
    private val commentsRepository: CommentsRepository
) {
    fun createReply(createReply: CreateReply): Long {
        return with(createReply) {
            Reply.create(
                writer = memberQuery.findOneByUUID(writerUUID!!),
                comment = commentsRepository.findOneById(commentId!!),
                content!!
            ).run { replyRepository.save(this).id!! }
        }
    }

    fun editReply(id: Long, updateReplyContent: UpdateReplyContent) {
        with(updateReplyContent) {
            replyRepository.findOneByIdAndWriter(id, writerUUID!!)
                .also { it.editContent(content!!) }
        }
    }

    fun removeReply(id: Long, removeReply: RemoveReply) {
        with(removeReply) {
            replyRepository.findOneByIdAndWriter(id, writerUUID!!)
                .also { replyRepository.delete(it) }
        }
    }
}