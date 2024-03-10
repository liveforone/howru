package howru.howru.subscribe.service.query

import howru.howru.subscribe.repository.SubscribeQuery
import howru.howru.subscribe.repository.SubscribeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class SubscribeQueryService @Autowired constructor(
    private val subscribeRepository: SubscribeRepository,
    private val subscribeQuery: SubscribeQuery
) {
    fun getSubscribesByFollower(followerId: UUID, page: Int) = subscribeQuery.findSubscribesByFollower(followerId, page)
    fun getSubscribesByFollowee(followeeId: UUID, page: Int) = subscribeQuery.findSubscribesByFollowee(followeeId, page)
    fun getCountOfFollower(followeeId: UUID) = subscribeRepository.countOfFollowersByFollowee(followeeId)
    fun getCountOfSubscribes(followerId: UUID) = subscribeRepository.countOfSubscribesByFollower(followerId)
    fun getFollowees(followerId: UUID) = subscribeQuery.findFollowees(followerId)
    fun isFollowee(followeeId: UUID, followerUUID: UUID) = subscribeRepository.isFollowee(followeeId, followerUUID)
    fun isFollowEach(followeeId: UUID, followerUUID: UUID) = subscribeRepository.isFollowEach(followeeId, followerUUID)
}