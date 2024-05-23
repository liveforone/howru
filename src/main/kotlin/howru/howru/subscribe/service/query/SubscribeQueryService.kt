package howru.howru.subscribe.service.query

import howru.howru.subscribe.repository.SubscribeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class SubscribeQueryService
    @Autowired
    constructor(
        private val subscribeRepository: SubscribeRepository
    ) {
        fun getFollowing(
            memberId: UUID,
            lastTimestamp: Int?
        ) = subscribeRepository.findFollowing(memberId, lastTimestamp)

        fun getFollower(
            memberId: UUID,
            lastTimestamp: Int?
        ) = subscribeRepository.findFollower(memberId, lastTimestamp)

        fun getCountOfFollower(memberId: UUID) = subscribeRepository.countOfFollowers(memberId)

        fun getCountOfFollowing(memberId: UUID) = subscribeRepository.countOfFollowings(memberId)

        fun getFollowees(followerId: UUID) = subscribeRepository.findFollowees(followerId)

        fun isFollowee(
            followeeId: UUID,
            followerUUID: UUID
        ) = subscribeRepository.isFollowee(followeeId, followerUUID)

        fun isFollowEach(
            followeeId: UUID,
            followerUUID: UUID
        ) = subscribeRepository.isFollowEach(followeeId, followerUUID)
    }
