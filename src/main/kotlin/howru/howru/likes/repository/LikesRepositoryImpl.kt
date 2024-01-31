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
    override fun findOneById(memberId: UUID, postId: Long): Likes {
        return try {
            queryFactory.singleQuery {
                select(entity(Likes::class))
                from(Likes::class)
                where(col(Likes::memberId).equal(memberId).and(col(Likes::postId).equal(postId)))
            }
        } catch (e: NoResultException) {
            throw LikesException(LikesExceptionMessage.LIKES_IS_NULL, postId)
        }
    }

    override fun countOfLikesByPost(postId: Long): Long {
        return queryFactory.singleQuery {
            select(count(entity(Likes::class)))
            from(Likes::class)
            where(col(Likes::postId).equal(postId))
        }
    }

    override fun findLikesBelongMember(memberId: UUID, lastPostId: Long?): List<LikesBelongMemberInfo> {
        return queryFactory.listQuery {
            select(listOf(col(Likes::postId)))
            from(Likes::class)
            where(col(Likes::memberId).equal(memberId))
            where(ltLastTimestampBelongMember(memberId, lastPostId))
            orderBy(col(Likes::timestamp).desc())
            limit(LikesRepoConstant.LIMIT_PAGE)
        }
    }

    override fun findLikesBelongPost(postId: Long, lastMemberId: UUID?): List<LikesBelongPostInfo> {
        return queryFactory.listQuery {
            select(listOf(col(Likes::memberId)))
            from(Likes::class)
            where(col(Likes::postId).equal(postId))
            where(ltLastTimestampBelongPost(postId, lastMemberId))
            orderBy(col(Likes::timestamp).desc())
            limit(LikesRepoConstant.LIMIT_PAGE)
        }
    }

    private fun findLastTimestampBelongMember(memberId: UUID, postId: Long): Int {
        return try {
            queryFactory.singleQuery {
                select(col(Likes::timestamp))
                from(Likes::class)
                where(col(Likes::memberId).equal(memberId).and(col(Likes::postId).equal(postId)))
            }
        } catch (e: NoResultException) {
            LikesRepoConstant.END_OF_TIMESTAMP
        }
    }

    private fun <T> SpringDataCriteriaQueryDsl<T>.ltLastTimestampBelongMember(memberId: UUID, postId: Long?): PredicateSpec? {
        val findTimestamp = postId?.let { findLastTimestampBelongMember(memberId, postId) }
        return findTimestamp?.let { and(col(Likes::timestamp).lessThan(it)) }
    }

    private fun findLastTimestampBelongPost(postId: Long, memberId: UUID): Int {
        return try {
            queryFactory.singleQuery {
                select(col(Likes::timestamp))
                from(Likes::class)
                where(col(Likes::memberId).equal(memberId).and(col(Likes::postId).equal(postId)))
            }
        } catch (e: NoResultException) {
            LikesRepoConstant.END_OF_TIMESTAMP
        }
    }

    private fun <T> SpringDataCriteriaQueryDsl<T>.ltLastTimestampBelongPost(postId: Long, memberId: UUID?): PredicateSpec? {
        val findTimestamp = memberId?.let { findLastTimestampBelongPost(postId, memberId) }
        return findTimestamp?.let { and(col(Likes::timestamp).lessThan(it)) }
    }
}