package howru.howru.reply.service.command

import howru.howru.comments.repository.CommentsRepository
import howru.howru.member.repository.MemberCustomRepository
import howru.howru.reply.domain.Reply
import howru.howru.reply.dto.CreateReply
import howru.howru.reply.dto.RemoveReply
import howru.howru.reply.dto.UpdateReplyContent
import howru.howru.reply.repository.ReplyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReplyCommandService
    @Autowired
    constructor(
        private val replyRepository: ReplyRepository,
        private val memberRepository: MemberCustomRepository,
        private val commentsRepository: CommentsRepository
    ) {
        fun createReply(createReply: CreateReply): Long {
            return with(createReply) {
                Reply.create(
                    writer = memberRepository.findMemberById(writerId!!),
                    comment = commentsRepository.findCommentById(commentId!!),
                    content!!
                ).run { replyRepository.save(this).id!! }
            }
        }

        fun editReply(
            id: Long,
            updateReplyContent: UpdateReplyContent
        ) {
            with(updateReplyContent) {
                replyRepository.findReplyByIdAndWriter(id, writerId!!)
                    .also { it.editContent(content!!) }
            }
        }

        fun removeReply(
            id: Long,
            removeReply: RemoveReply
        ) {
            with(removeReply) {
                replyRepository.findReplyByIdAndWriter(id, writerId!!)
                    .also { replyRepository.delete(it) }
            }
        }
    }
