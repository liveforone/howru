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
        fun getSubscribesByFollower(
            followerId: UUID,
            lastTimestamp: Int?
        ) = subscribeRepository.findSubscribesByFollower(followerId, lastTimestamp)

        fun getSubscribesByFollowee(
            followeeId: UUID,
            lastTimestamp: Int?
        ) = subscribeRepository.findSubscribesByFollowee(
            followeeId,
            lastTimestamp
        )

        fun getCountOfFollower(followeeId: UUID) = subscribeRepository.countOfFollowersByFollowee(followeeId)

        fun getCountOfSubscribes(followerId: UUID) = subscribeRepository.countOfSubscribesByFollower(followerId)

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
