package howru.howru.comments.service.query

import howru.howru.comments.dto.response.CommentsInfo
import howru.howru.comments.log.CommentsServiceLog
import howru.howru.comments.repository.CommentsRepository
import howru.howru.exception.exception.SubscribeException
import howru.howru.exception.message.SubscribeExceptionMessage
import howru.howru.logger
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
    fun getCommentById(id: Long) = commentsRepository.findOneDtoById(id)
    fun getCommentsByWriter(writerId: UUID, lastId: Long?) = commentsRepository.findCommentsByWriter(writerId, lastId)
    fun getCommentsByPost(postId: Long, lastId: Long?) = commentsRepository.findCommentsByPost(postId, lastId)
    fun getCommentsBySomeone(someoneId: UUID, memberId: UUID, lastId: Long?): List<CommentsInfo> {
        require(subscribeQueryService.isFollowEach(someoneId, memberId)) {
            logger().info(CommentsServiceLog.VIEW_SOMEONE_COMMENTS_WHO_NOT_FOLLOWING + memberId)
            throw SubscribeException(SubscribeExceptionMessage.IS_NOT_FOLLOW_EACH, memberId)
        }
        return commentsRepository.findCommentsByWriter(someoneId, lastId)
    }
}