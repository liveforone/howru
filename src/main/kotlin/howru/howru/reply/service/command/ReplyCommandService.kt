package howru.howru.reply.service.command

import howru.howru.comments.repository.CommentsRepository
import howru.howru.member.repository.MemberRepository
import howru.howru.reply.domain.Reply
import howru.howru.reply.dto.request.CreateReply
import howru.howru.reply.dto.request.DeleteReply
import howru.howru.reply.dto.update.UpdateReplyContent
import howru.howru.reply.repository.ReplyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class ReplyCommandService @Autowired constructor(
    private val replyRepository: ReplyRepository,
    private val memberRepository: MemberRepository,
    private val commentsRepository: CommentsRepository
) {
    fun createReply(createReply: CreateReply): UUID {
        return with(createReply) {
            Reply.create(
                writer = memberRepository.findOneByUUID(writerUUID!!),
                comment = commentsRepository.findOneByUUID(commentUUID!!),
                content!!
            ).run { replyRepository.save(this).uuid }
        }
    }

    fun editReply(updateReplyContent: UpdateReplyContent) {
        with(updateReplyContent) {
            replyRepository.findOneByUUIDAndWriter(uuid!!, writerUUID!!)
                .also { it.editContent(content!!) }
        }
    }

    fun deleteReply(deleteReply: DeleteReply) {
        with(deleteReply) {
            replyRepository.findOneByUUIDAndWriter(uuid!!, writerUUID!!)
                .also { replyRepository.delete(it) }
        }
    }
}