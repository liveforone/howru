package howru.howru.subscribe.repository

import howru.howru.subscribe.domain.Subscribe
import howru.howru.subscribe.dto.response.SubscribeInfo
import java.util.*
import kotlin.collections.List

interface SubscribeCustomRepository {
    fun findOneById(followeeId: UUID, followerId: UUID): Subscribe
    fun findSubscribesByFollower(followerId: UUID, lastFolloweeId: UUID?, lastFollowerId: UUID?): List<SubscribeInfo>
    fun findSubscribesByFollowee(followeeId: UUID, lastFolloweeId: UUID?, lastFollowerId: UUID?): List<SubscribeInfo>
    fun findFollowees(followerId: UUID): List<UUID>
    fun countOfSubscribesByFollower(followerId: UUID): Long
    fun countOfFollowersByFollowee(followeeId: UUID): Long
}