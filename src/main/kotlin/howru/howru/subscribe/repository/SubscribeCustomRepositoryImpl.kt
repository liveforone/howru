package howru.howru.subscribe.repository

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import howru.howru.subscribe.exception.SubscribeException
import howru.howru.subscribe.exception.SubscribeExceptionMessage
import howru.howru.global.util.ltLastTimestamp
import howru.howru.subscribe.domain.QSubscribe
import howru.howru.subscribe.domain.Subscribe
import howru.howru.subscribe.dto.response.SubscribeInfo
import howru.howru.subscribe.repository.constant.SubscribeRepoConstant
import java.util.*

class SubscribeCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val subscribe: QSubscribe = QSubscribe.subscribe
) : SubscribeCustomRepository {
    override fun findSubscribeById(
        followeeId: UUID,
        followerId: UUID
    ): Subscribe =
        jpaQueryFactory
            .selectFrom(subscribe)
            .where(subscribe.followeeId.eq(followeeId).and(subscribe.followerId.eq(followerId)))
            .fetchOne() ?: throw SubscribeException(SubscribeExceptionMessage.SUBSCRIBE_IS_NULL, followerId)

    private val subscribeInfoField =
        Projections.constructor(
            SubscribeInfo::class.java,
            subscribe.followeeId,
            subscribe.followerId
        )

    override fun findFollowing(
        memberId: UUID,
        lastTimestamp: Int?
    ): List<SubscribeInfo> =
        jpaQueryFactory
            .select(subscribeInfoField)
            .from(subscribe)
            .where(subscribe.followerId.eq(memberId).and(ltLastTimestamp(lastTimestamp, subscribe) { it.timestamp }))
            .orderBy(subscribe.timestamp.desc())
            .limit(SubscribeRepoConstant.LIMIT_PAGE)
            .fetch()

    override fun findFollower(
        memberId: UUID,
        lastTimestamp: Int?
    ): List<SubscribeInfo> =
        jpaQueryFactory
            .select(subscribeInfoField)
            .from(subscribe)
            .where(subscribe.followeeId.eq(memberId).and(ltLastTimestamp(lastTimestamp, subscribe) { it.timestamp }))
            .orderBy(subscribe.timestamp.desc())
            .limit(SubscribeRepoConstant.LIMIT_PAGE)
            .fetch()

    override fun findFollowees(followerId: UUID): List<UUID> =
        jpaQueryFactory
            .select(subscribe.followeeId)
            .from(subscribe)
            .where(subscribe.followerId.eq(followerId))
            .orderBy(subscribe.timestamp.desc())
            .fetch()
}
