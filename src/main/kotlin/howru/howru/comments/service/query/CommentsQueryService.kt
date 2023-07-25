package howru.howru.comments.service.query

import howru.howru.comments.repository.CommentsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class CommentsQueryService @Autowired constructor(
    private val commentsRepository: CommentsRepository
) {
    fun getCommentByUUID(uuid: UUID) = commentsRepository.findOneDtoByUUID(uuid)
    fun getCommentsByWriter(writerUUID: UUID, lastUUID: UUID?) = commentsRepository.findCommentsByWriter(writerUUID, lastUUID)
    fun getCommentsByPost(postUUID: UUID, lastUUID: UUID?) = commentsRepository.findCommentsByPost(postUUID, lastUUID)
}