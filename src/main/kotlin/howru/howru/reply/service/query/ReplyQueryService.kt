package howru.howru.reply.service.query

import howru.howru.reply.repository.ReplyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class ReplyQueryService @Autowired constructor(
    private val replyRepository: ReplyRepository
) {
    fun getReplyById(id: Long) = replyRepository.findReplyInfoById(id)
    fun getRepliesByWriter(writerId: UUID, lastId: Long?) = replyRepository.findRepliesByWriter(writerId, lastId)
    fun getRepliesByComment(commentId: Long, lastId: Long?) = replyRepository.findRepliesByComment(commentId, lastId)
}