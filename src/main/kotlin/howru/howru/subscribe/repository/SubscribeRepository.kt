package howru.howru.subscribe.repository

import howru.howru.subscribe.domain.Subscribe
import howru.howru.subscribe.domain.SubscribePk
import howru.howru.subscribe.repository.query.SubscribeQuery
import howru.howru.subscribe.repository.query.SubscribeRepoParam
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface SubscribeRepository : JpaRepository<Subscribe, SubscribePk>, SubscribeCustomRepository {
    @Query(SubscribeQuery.FOLLOW_EACH)
    fun isFollowEach(@Param(SubscribeRepoParam.FOLLOWEE_UUID) followeeUUID: UUID, @Param(SubscribeRepoParam.FOLLOWER_UUID) followerUUID: UUID): Boolean
}