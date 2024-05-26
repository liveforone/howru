package howru.howru.member.service.integrated

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
        private val postQueryService: PostQueryService,
        private val subscribeQueryService: SubscribeQueryService
    ) {
        fun getPostOfOtherMember(
            writerId: UUID,
            myId: UUID,
            lastId: Long?
        ): PostPage {
            val writer = memberQueryService.getMemberById(writerId)
            return if (writer.isUnlock() || subscribeQueryService.isFollowee(writerId, myId)) {
                postQueryService.getPostsByMember(writerId, lastId)
            } else {
                logger().warn(IntegratedMemberServiceLog.NOT_FOLLOWER + writerId)
                throw SubscribeException(SubscribeExceptionMessage.NOT_FOLLOWER, myId)
            }
        }

        fun getCountOfPostByMember(memberId: UUID) = postQueryService.getCountOfPostByMember(memberId)
    }
