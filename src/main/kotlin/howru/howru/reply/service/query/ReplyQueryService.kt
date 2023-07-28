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
    fun getReplyByUUID(uuid: UUID) = replyRepository.findOneDtoByUUID(uuid)
    fun getRepliesByWriter(writerUUID: UUID, lastUUID: UUID?) = replyRepository.findRepliesByWriter(writerUUID, lastUUID)
    fun getRepliesByComment(commentUUID: UUID, lastUUID: UUID?) = replyRepository.findRepliesByComment(commentUUID, lastUUID)
}