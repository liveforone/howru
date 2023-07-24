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

    override fun findOneByUUID(followeeUUID: UUID, followerUUID: UUID): Subscribe {
        return try {
            queryFactory.singleQuery {
                select(entity(Subscribe::class))
                from(Subscribe::class)
                where(col(Subscribe::followeeUUID).equal(followeeUUID).and(col(Subscribe::followerUUID).equal(followerUUID)))
            }
        } catch (e: NoResultException) {
            throw SubscribeException(SubscribeExceptionMessage.SUBSCRIBE_IS_NULL)
        }
    }

    override fun findSubscribesByFollower(followerUUID: UUID, lastFolloweeUUID: UUID?, lastFollowerUUID: UUID?): List<SubscribeInfo> {
        return queryFactory.listQuery {
            select(listOf(
                col(Subscribe::followeeUUID),
                col(Subscribe::followerUUID)
            ))
            from(Subscribe::class)
            where(col(Subscribe::followerUUID).equal(followerUUID))
            where(ltLastTimestamp(lastFolloweeUUID, lastFollowerUUID))
            orderBy(col(Subscribe::timestamp).desc())
            limit(SubscribeRepoConstant.LIMIT_PAGE)
        }
    }

    override fun findSubscribesByFollowee(followeeUUID: UUID, lastFolloweeUUID: UUID?, lastFollowerUUID: UUID?): List<SubscribeInfo> {
        return queryFactory.listQuery {
            select(listOf(
                col(Subscribe::followeeUUID),
                col(Subscribe::followerUUID)
            ))
            from(Subscribe::class)
            where(col(Subscribe::followeeUUID).equal(followeeUUID))
            where(ltLastTimestamp(lastFolloweeUUID, lastFollowerUUID))
            orderBy(col(Subscribe::timestamp).desc())
            limit(SubscribeRepoConstant.LIMIT_PAGE)
        }
    }

    override fun findFollowee(followerUUID: UUID): List<UUID> {
        return queryFactory.listQuery {
            select(col(Subscribe::followeeUUID))
            from(Subscribe::class)
            where(col(Subscribe::followerUUID).equal(followerUUID))
            orderBy(col(Subscribe::timestamp).desc())
        }
    }

    override fun countSubscribesByFollower(followerUUID: UUID): Long {
        return queryFactory.singleQuery {
            select(count(entity(Subscribe::class)))
            from(Subscribe::class)
            where(col(Subscribe::followerUUID).equal(followerUUID))
        }
    }

    override fun countFollowersByFollowee(followeeUUID: UUID): Long {
        return queryFactory.singleQuery {
            select(count(entity(Subscribe::class)))
            from(Subscribe::class)
            where(col(Subscribe::followeeUUID).equal(followeeUUID))
        }
    }

    private fun findLastTimestamp(lastFolloweeUUID: UUID, lastFollowerUUID: UUID): Int {
        return queryFactory.singleQuery {
            select(listOf(col(Subscribe::timestamp)))
            from(Subscribe::class)
            where(col(Subscribe::followeeUUID).equal(lastFolloweeUUID).and(col(Subscribe::followerUUID).equal(lastFollowerUUID)))
        }
    }

    private fun <T> SpringDataCriteriaQueryDsl<T>.ltLastTimestamp(lastFolloweeUUID: UUID?, lastFollowerUUID: UUID?): PredicateSpec? {
        val findTimestamp = lastFolloweeUUID?.let { followee ->
            lastFollowerUUID?.let { follower ->
                findLastTimestamp(followee, follower)
            }
        }
        return findTimestamp?.let { and(col(Subscribe::timestamp).lessThan(it)) }
    }
}