package howru.howru.reply.service.query

import howru.howru.reply.repository.ReplyQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class ReplyQueryService @Autowired constructor(
    private val replyQuery: ReplyQuery
) {
    fun getReplyById(id: Long) = replyQuery.findOneDtoById(id)
    fun getRepliesByWriter(writerId: UUID, page: Int) = replyQuery.findRepliesByWriter(writerId, page)
    fun getRepliesByComment(commentId: Long, page: Int) = replyQuery.findRepliesByComment(commentId, page)
}