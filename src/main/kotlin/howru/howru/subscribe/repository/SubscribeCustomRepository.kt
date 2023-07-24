package howru.howru.subscribe.repository

import howru.howru.subscribe.domain.Subscribe
import howru.howru.subscribe.dto.response.SubscribeInfo
import java.util.*
import kotlin.collections.List

interface SubscribeCustomRepository {
    fun findOneByUUID(followeeUUID: UUID, followerUUID: UUID): Subscribe
    fun findSubscribesByFollower(followerUUID: UUID, lastFolloweeUUID: UUID?, lastFollowerUUID: UUID?): List<SubscribeInfo>
    fun findSubscribesByFollowee(followeeUUID: UUID, lastFolloweeUUID: UUID?, lastFollowerUUID: UUID?): List<SubscribeInfo>
    fun findFollowee(followerUUID: UUID): List<UUID>
    fun countSubscribesByFollower(followerUUID: UUID): Long
    fun countFollowersByFollowee(followeeUUID: UUID): Long
}