package howru.howru.likes.repository

import com.linecorp.kotlinjdsl.query.spec.predicate.PredicateSpec
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.listQuery
import com.linecorp.kotlinjdsl.spring.data.querydsl.SpringDataCriteriaQueryDsl
import com.linecorp.kotlinjdsl.spring.data.singleQuery
import howru.howru.exception.exception.LikesException
import howru.howru.exception.message.LikesExceptionMessage
import howru.howru.likes.domain.Likes
import howru.howru.likes.dto.response.LikesBelongMemberInfo
import howru.howru.likes.dto.response.LikesBelongPostInfo
import howru.howru.likes.repository.constant.LikesRepoConstant
import jakarta.persistence.NoResultException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class LikesRepositoryImpl @Autowired constructor(
    private val queryFactory: SpringDataQueryFactory
) : LikesCustomRepository {
    override fun findOneByUUID(memberUUID: UUID, postUUID: UUID): Likes {
        return try {
            queryFactory.singleQuery {
                select(entity(Likes::class))
                from(Likes::class)
                where(col(Likes::memberUUID).equal(memberUUID).and(col(Likes::postUUID).equal(postUUID)))
            }
        } catch (e: NoResultException) {
            throw LikesException(LikesExceptionMessage.LIKES_IS_NULL)
        }
    }

    override fun findLikesBelongMember(memberUUID: UUID, lastPostUUID: UUID?): List<LikesBelongMemberInfo> {
        return queryFactory.listQuery {
            select(listOf(col(Likes::postUUID)))
            from(Likes::class)
            where(col(Likes::memberUUID).equal(memberUUID))
            where(ltLastTimestampBelongMember(memberUUID, lastPostUUID))
            orderBy(col(Likes::timestamp).desc())
            limit(LikesRepoConstant.LIMIT_PAGE)
        }
    }

    override fun findLikesBelongPost(postUUID: UUID, lastMemberUUID: UUID?): List<LikesBelongPostInfo> {
        return queryFactory.listQuery {
            select(listOf(col(Likes::memberUUID)))
            from(Likes::class)
            where(col(Likes::postUUID).equal(postUUID))
            where(ltLastTimestampBelongPost(postUUID, lastMemberUUID))
            orderBy(col(Likes::timestamp).desc())
            limit(LikesRepoConstant.LIMIT_PAGE)
        }
    }

    private fun findLastTimestampBelongMember(memberUUID: UUID, postUUID: UUID): Int {
        return try {
            queryFactory.singleQuery {
                select(col(Likes::timestamp))
                from(Likes::class)
                where(col(Likes::memberUUID).equal(memberUUID).and(col(Likes::postUUID).equal(postUUID)))
            }
        } catch (e: NoResultException) {
            LikesRepoConstant.END_OF_TIMESTAMP
        }
    }

    private fun <T> SpringDataCriteriaQueryDsl<T>.ltLastTimestampBelongMember(memberUUID: UUID, postUUID: UUID?): PredicateSpec? {
        val findTimestamp = postUUID?.let { findLastTimestampBelongMember(postUUID, memberUUID) }
        return findTimestamp?.let { and(col(Likes::timestamp).lessThan(it)) }
    }

    private fun findLastTimestampBelongPost(postUUID: UUID, memberUUID: UUID): Int {
        return try {
            queryFactory.singleQuery {
                select(col(Likes::timestamp))
                from(Likes::class)
                where(col(Likes::memberUUID).equal(memberUUID).and(col(Likes::postUUID).equal(postUUID)))
            }
        } catch (e: NoResultException) {
            LikesRepoConstant.END_OF_TIMESTAMP
        }
    }

    private fun <T> SpringDataCriteriaQueryDsl<T>.ltLastTimestampBelongPost(postUUID: UUID, memberUUID: UUID?): PredicateSpec? {
        val findTimestamp = memberUUID?.let { findLastTimestampBelongPost(postUUID, memberUUID) }
        return findTimestamp?.let { and(col(Likes::timestamp).lessThan(it)) }
    }
}