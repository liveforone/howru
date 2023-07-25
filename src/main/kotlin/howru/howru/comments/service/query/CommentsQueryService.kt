package howru.howru.comments.service.query

import howru.howru.comments.dto.response.CommentsInfo
import howru.howru.comments.repository.CommentsRepository
import howru.howru.exception.exception.CommentsException
import howru.howru.exception.message.CommentsExceptionMessage
import howru.howru.subscribe.service.query.SubscribeQueryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class CommentsQueryService @Autowired constructor(
    private val commentsRepository: CommentsRepository,
    private val subscribeQueryService: SubscribeQueryService
) {
    fun getCommentByUUID(uuid: UUID) = commentsRepository.findOneDtoByUUID(uuid)
    fun getCommentsByWriter(writerUUID: UUID, lastUUID: UUID?) = commentsRepository.findCommentsByWriter(writerUUID, lastUUID)
    fun getCommentsByPost(postUUID: UUID, lastUUID: UUID?) = commentsRepository.findCommentsByPost(postUUID, lastUUID)
    fun getCommentsBySomeone(someoneUUID: UUID, memberUUID: UUID, lastUUID: UUID?): List<CommentsInfo> {
        require(subscribeQueryService.isFollowEach(someoneUUID, memberUUID)) { throw CommentsException(CommentsExceptionMessage.IS_NOT_FOLLOW_EACH) }
        return commentsRepository.findCommentsByWriter(someoneUUID, lastUUID)
    }
}