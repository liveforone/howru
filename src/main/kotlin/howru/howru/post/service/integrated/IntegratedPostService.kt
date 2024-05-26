package howru.howru.post.service.integrated

import howru.howru.logger
import howru.howru.member.service.query.MemberQueryService
import howru.howru.post.dto.response.PostPage
import howru.howru.post.log.IntegratedPostServiceLog
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
class IntegratedPostService
    @Autowired
    constructor(
        private val memberQueryService: MemberQueryService,
        private val subscribeQueryService: SubscribeQueryService,
        private val postQueryService: PostQueryService
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
                logger().warn(IntegratedPostServiceLog.NOT_FOLLOWER + memberId)
                throw SubscribeException(SubscribeExceptionMessage.NOT_FOLLOWER, myId)
            }
        }
    }
