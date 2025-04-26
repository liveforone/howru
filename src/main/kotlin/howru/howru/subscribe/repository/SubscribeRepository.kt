package howru.howru.subscribe.repository

import howru.howru.subscribe.domain.Subscribe
import howru.howru.subscribe.domain.SubscribePk
import howru.howru.subscribe.repository.sql.SubscribeSql
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface SubscribeRepository :
    JpaRepository<Subscribe, SubscribePk>,
    SubscribeCustomRepository {
    @Query(SubscribeSql.IS_FOLLOWEE)
    fun isFollowee(
        @Param(SubscribeSql.FOLLOWEE_ID) followeeId: UUID,
        @Param(SubscribeSql.FOLLOWER_ID) followerId: UUID
    ): Boolean

    @Query(SubscribeSql.IS_FOLLOW_EACH)
    fun isFollowEach(
        @Param(SubscribeSql.FOLLOWEE_ID) followeeId: UUID,
        @Param(SubscribeSql.FOLLOWER_ID) followerId: UUID
    ): Boolean

    @Query("select count(*) from Subscribe s where s.followerId = :followerId")
    fun countOfFollowings(
        @Param("followerId") followerId: UUID
    ): Long

    @Query("select count(*) from Subscribe s where s.followeeId = :followeeId")
    fun countOfFollowers(
        @Param("followeeId") followeeId: UUID
    ): Long
}
