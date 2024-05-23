package howru.howru.subscribe.repository

import howru.howru.subscribe.domain.Subscribe
import howru.howru.subscribe.dto.response.SubscribeInfo
import java.util.*

interface SubscribeCustomRepository {
    fun findSubscribeById(
        followeeId: UUID,
        followerId: UUID
    ): Subscribe

    fun findFollowing(
        memberId: UUID,
        lastTimestamp: Int?
    ): List<SubscribeInfo>

    fun findFollower(
        memberId: UUID,
        lastTimestamp: Int?
    ): List<SubscribeInfo>

    fun findFollowees(followerId: UUID): List<UUID>
}
