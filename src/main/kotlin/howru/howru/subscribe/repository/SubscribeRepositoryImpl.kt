package howru.howru.subscribe.repository

import com.linecorp.kotlinjdsl.query.spec.predicate.PredicateSpec
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.listQuery
import com.linecorp.kotlinjdsl.spring.data.querydsl.SpringDataCriteriaQueryDsl
import com.linecorp.kotlinjdsl.spring.data.singleQuery
import howru.howru.exception.exception.SubscribeException
import howru.howru.exception.message.SubscribeExceptionMessage
import howru.howru.subscribe.domain.Subscribe
import howru.howru.subscribe.dto.response.SubscribeInfo
import howru.howru.subscribe.repository.constant.SubscribeRepoConstant
import jakarta.persistence.NoResultException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class SubscribeRepositoryImpl @Autowired constructor(
    private val queryFactory: SpringDataQueryFactory
) : SubscribeCustomRepository {

    override fun findOneById(followeeId: UUID, followerId: UUID): Subscribe {
        return try {
            queryFactory.singleQuery {
                select(entity(Subscribe::class))
                from(Subscribe::class)
                where(
                    col(Subscribe::followeeId).equal(followeeId)
                        .and(col(Subscribe::followerId).equal(followerId))
                )
            }
        } catch (e: NoResultException) {
            throw SubscribeException(SubscribeExceptionMessage.SUBSCRIBE_IS_NULL, followerId)
        }
    }

    override fun findSubscribesByFollower(followerId: UUID, lastFolloweeId: UUID?, lastFollowerId: UUID?): List<SubscribeInfo> {
        return queryFactory.listQuery {
            select(listOf(
                col(Subscribe::followeeId),
                col(Subscribe::followerId)
            ))
            from(Subscribe::class)
            where(col(Subscribe::followerId).equal(followerId))
            where(ltLastTimestamp(lastFolloweeId, lastFollowerId))
            orderBy(col(Subscribe::timestamp).desc())
            limit(SubscribeRepoConstant.LIMIT_PAGE)
        }
    }

    override fun findSubscribesByFollowee(followeeId: UUID, lastFolloweeId: UUID?, lastFollowerId: UUID?): List<SubscribeInfo> {
        return queryFactory.listQuery {
            select(listOf(
                col(Subscribe::followeeId),
                col(Subscribe::followerId)
            ))
            from(Subscribe::class)
            where(col(Subscribe::followeeId).equal(followeeId))
            where(ltLastTimestamp(lastFolloweeId, lastFollowerId))
            orderBy(col(Subscribe::timestamp).desc())
            limit(SubscribeRepoConstant.LIMIT_PAGE)
        }
    }

    override fun findFollowees(followerId: UUID): List<UUID> {
        return queryFactory.listQuery {
            select(col(Subscribe::followeeId))
            from(Subscribe::class)
            where(col(Subscribe::followerId).equal(followerId))
            orderBy(col(Subscribe::timestamp).desc())
        }
    }

    override fun countOfSubscribesByFollower(followerId: UUID): Long {
        return queryFactory.singleQuery {
            select(count(entity(Subscribe::class)))
            from(Subscribe::class)
            where(col(Subscribe::followerId).equal(followerId))
        }
    }

    override fun countOfFollowersByFollowee(followeeId: UUID): Long {
        return queryFactory.singleQuery {
            select(count(entity(Subscribe::class)))
            from(Subscribe::class)
            where(col(Subscribe::followeeId).equal(followeeId))
        }
    }

    private fun findLastTimestamp(lastFolloweeId: UUID, lastFollowerId: UUID): Int {
        return try {
            queryFactory.singleQuery {
                select(listOf(col(Subscribe::timestamp)))
                from(Subscribe::class)
                where(
                    col(Subscribe::followeeId).equal(lastFolloweeId)
                        .and(col(Subscribe::followerId).equal(lastFollowerId))
                )
            }
        } catch (e: NoResultException) {
            SubscribeRepoConstant.END_OF_TIMESTAMP
        }
    }

    private fun <T> SpringDataCriteriaQueryDsl<T>.ltLastTimestamp(lastFolloweeId: UUID?, lastFollowerId: UUID?): PredicateSpec? {
        val findTimestamp = lastFolloweeId?.let { followee ->
            lastFollowerId?.let { follower ->
                findLastTimestamp(followee, follower)
            }
        }
        return findTimestamp?.let { and(col(Subscribe::timestamp).lessThan(it)) }
    }
}