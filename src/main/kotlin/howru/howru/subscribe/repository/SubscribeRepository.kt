package howru.howru.subscribe.repository

import howru.howru.subscribe.domain.Subscribe
import howru.howru.subscribe.domain.SubscribePk
import howru.howru.subscribe.repository.sql.SubscribeSql
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface SubscribeRepository : JpaRepository<Subscribe, SubscribePk>, SubscribeCustomRepository {

    @Query(SubscribeSql.IS_FOLLOWEE)
    fun isFollowee(@Param(SubscribeSql.FOLLOWEE_UUID) followeeUUID: UUID, @Param(SubscribeSql.FOLLOWER_UUID) followerUUID: UUID): Boolean

    @Query(SubscribeSql.IS_FOLLOW_EACH)
    fun isFollowEach(@Param(SubscribeSql.FOLLOWEE_UUID) followeeUUID: UUID, @Param(SubscribeSql.FOLLOWER_UUID) followerUUID: UUID): Boolean
}