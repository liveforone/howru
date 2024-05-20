package howru.howru.subscribe.repository

import howru.howru.subscribe.domain.Subscribe
import howru.howru.subscribe.domain.vo.SubscribeInfo
import java.util.*

interface SubscribeCustomRepository {
    fun findSubscribeById(
        followeeId: UUID,
        followerId: UUID
    ): Subscribe

    fun findSubscribesByFollower(
        followerId: UUID,
        lastTimestamp: Int?
    ): List<SubscribeInfo>

    fun findSubscribesByFollowee(
        followeeId: UUID,
        lastTimestamp: Int?
    ): List<SubscribeInfo>

    fun findFollowees(followerId: UUID): List<UUID>
}
