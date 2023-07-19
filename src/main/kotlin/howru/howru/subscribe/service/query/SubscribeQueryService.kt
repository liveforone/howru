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
    fun getSubscribesByFollower(followerUUID: UUID) = subscribeRepository.findSubscribesByFollower(followerUUID)
    fun getSubscribesByFollowee(followeeUUID: UUID) = subscribeRepository.findSubscribesByFollowee(followeeUUID)
    fun getCountFollower(followeeUUID: UUID) = subscribeRepository.countFollowersByFollowee(followeeUUID)
    fun getCountSubscribes(followerUUID: UUID) = subscribeRepository.countSubscribesByFollower(followerUUID)
    fun isFollowEach(followeeUUID: UUID, followerUUID: UUID) = subscribeRepository.isFollowEach(followeeUUID, followerUUID)
    fun getFollowee(followerUUID: UUID) = subscribeRepository.findFollowee(followerUUID)
}