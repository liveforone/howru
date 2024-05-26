package howru.howru.comments.service.integrated

import howru.howru.comments.dto.response.CommentsPage
import howru.howru.comments.log.IntegratedCommentsServiceLog
import howru.howru.comments.service.query.CommentsQueryService
import howru.howru.logger
import howru.howru.subscribe.exception.SubscribeException
import howru.howru.subscribe.exception.SubscribeExceptionMessage
import howru.howru.subscribe.service.query.SubscribeQueryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class IntegratedCommentsService
    @Autowired
    constructor(
        private val subscribeQueryService: SubscribeQueryService,
        private val commentsQueryService: CommentsQueryService
    ) {
        fun getCommentsByOtherMember(
            memberId: UUID,
            myId: UUID,
            lastId: Long?
        ): CommentsPage {
            require(subscribeQueryService.isFollowEach(memberId, myId)) {
                logger().info(IntegratedCommentsServiceLog.VIEW_SOMEONE_COMMENTS_WHO_NOT_FOLLOWING + myId)
                throw SubscribeException(SubscribeExceptionMessage.IS_NOT_FOLLOW_EACH, myId)
            }
            return commentsQueryService.getCommentsByMember(memberId, lastId)
        }
    }
