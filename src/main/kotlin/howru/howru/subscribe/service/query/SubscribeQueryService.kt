package howru.howru.subscribe.service.query

import howru.howru.subscribe.repository.SubscribeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class SubscribeQueryService @Autowired constructor(
    private val subscribeRepository: SubscribeRepository
) {
    fun getSubscribesByFollower(followerUUID: UUID, lastFolloweeUUID: UUID?, lastFollowerUUID: UUID?) = subscribeRepository.findSubscribesByFollower(followerUUID, lastFolloweeUUID, lastFollowerUUID)
    fun getSubscribesByFollowee(followeeUUID: UUID, lastFolloweeUUID: UUID?, lastFollowerUUID: UUID?) = subscribeRepository.findSubscribesByFollowee(followeeUUID, lastFolloweeUUID, lastFollowerUUID)
    fun getCountOfFollower(followeeUUID: UUID) = subscribeRepository.countOfFollowersByFollowee(followeeUUID)
    fun getCountOfSubscribes(followerUUID: UUID) = subscribeRepository.countOfSubscribesByFollower(followerUUID)
    fun getFollowees(followerUUID: UUID) = subscribeRepository.findFollowees(followerUUID)
    fun isFollowee(followeeUUID: UUID, followerUUID: UUID) = subscribeRepository.isFollowee(followeeUUID, followerUUID)
    fun isFollowEach(followeeUUID: UUID, followerUUID: UUID) = subscribeRepository.isFollowEach(followeeUUID, followerUUID)
}