package howru.howru.member.service.integrated

import howru.howru.comments.dto.response.CommentsPage
import howru.howru.comments.service.query.CommentsQueryService
import howru.howru.logger
import howru.howru.member.log.IntegratedMemberServiceLog
import howru.howru.member.service.query.MemberQueryService
import howru.howru.post.dto.response.PostPage
import howru.howru.post.service.query.PostQueryService
import howru.howru.subscribe.exception.SubscribeException
import howru.howru.subscribe.exception.SubscribeExceptionMessage
import howru.howru.subscribe.service.query.SubscribeQueryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class IntegratedMemberService
    @Autowired
    constructor(
        private val memberQueryService: MemberQueryService,
        private val subscribeQueryService: SubscribeQueryService,
        private val postQueryService: PostQueryService,
        private val commentsQueryService: CommentsQueryService
    ) {
        fun getPostOfOtherMember(
            memberId: UUID,
            myId: UUID,
            lastId: Long?
        ): PostPage {
            val writer = memberQueryService.getMemberById(memberId)
            return if (writer.isUnlock() || subscribeQueryService.isFollowee(memberId, myId)) {
                postQueryService.getPostsByMember(memberId, lastId)
            } else {
                logger().warn(IntegratedMemberServiceLog.NOT_FOLLOWER + memberId)
                throw SubscribeException(SubscribeExceptionMessage.NOT_FOLLOWER, myId)
            }
        }

        fun getCountOfPostByMember(memberId: UUID) = postQueryService.getCountOfPostByMember(memberId)

        fun getCommentsByOtherMember(
            memberId: UUID,
            myId: UUID,
            lastId: Long?
        ): CommentsPage {
            require(subscribeQueryService.isFollowEach(memberId, myId)) {
                logger().info(IntegratedMemberServiceLog.VIEW_SOMEONE_COMMENTS_WHO_NOT_FOLLOWING + myId)
                throw SubscribeException(SubscribeExceptionMessage.IS_NOT_FOLLOW_EACH, myId)
            }
            return commentsQueryService.getCommentsByMember(memberId, lastId)
        }
    }
