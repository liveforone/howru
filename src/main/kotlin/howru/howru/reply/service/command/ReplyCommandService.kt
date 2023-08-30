package howru.howru.reply.service.command

import howru.howru.comments.repository.CommentsRepository
import howru.howru.member.repository.MemberRepository
import howru.howru.reply.domain.Reply
import howru.howru.reply.dto.request.CreateReply
import howru.howru.reply.dto.request.RemoveReply
import howru.howru.reply.dto.update.UpdateReplyContent
import howru.howru.reply.repository.ReplyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReplyCommandService @Autowired constructor(
    private val replyRepository: ReplyRepository,
    private val memberRepository: MemberRepository,
    private val commentsRepository: CommentsRepository
) {
    fun createReply(createReply: CreateReply): Long {
        return with(createReply) {
            Reply.create(
                writer = memberRepository.findOneByUUID(writerUUID!!),
                comment = commentsRepository.findOneById(commentId!!),
                content!!
            ).run { replyRepository.save(this).id!! }
        }
    }

    fun editReply(updateReplyContent: UpdateReplyContent) {
        with(updateReplyContent) {
            replyRepository.findOneByIdAndWriter(id!!, writerUUID!!)
                .also { it.editContent(content!!) }
        }
    }

    fun removeReply(removeReply: RemoveReply) {
        with(removeReply) {
            replyRepository.findOneByIdAndWriter(id!!, writerUUID!!)
                .also { replyRepository.delete(it) }
        }
    }
}