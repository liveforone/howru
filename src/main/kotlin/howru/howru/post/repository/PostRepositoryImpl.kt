package howru.howru.post.repository

import com.linecorp.kotlinjdsl.query.spec.predicate.PredicateSpec
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.from.fetch
import com.linecorp.kotlinjdsl.querydsl.from.join
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.listQuery
import com.linecorp.kotlinjdsl.spring.data.querydsl.SpringDataCriteriaQueryDsl
import com.linecorp.kotlinjdsl.spring.data.singleQuery
import howru.howru.exception.exception.PostException
import howru.howru.exception.message.PostExceptionMessage
import howru.howru.member.domain.Member
import howru.howru.post.domain.Post
import howru.howru.post.dto.response.PostInfo
import howru.howru.post.repository.constant.PostRepoConstant
import jakarta.persistence.NoResultException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*
import kotlin.random.Random

@Repository
class PostRepositoryImpl @Autowired constructor(
    private val queryFactory: SpringDataQueryFactory
) : PostCustomRepository {
    override fun findOneByUUID(uuid: UUID): Post {
        return try {
            queryFactory.singleQuery {
                select(entity(Post::class))
                from(Post::class)
                where(col(Post::uuid).equal(uuid))
            }
        } catch (e: NoResultException) {
            throw PostException(PostExceptionMessage.POST_IS_NULL)
        }
    }

    override fun findOneByUUIDAndWriter(uuid: UUID, writerUUID: UUID): Post {
        return try {
            queryFactory.singleQuery {
                select(entity(Post::class))
                from(Post::class)
                fetch(Post::writer)
                join(Post::writer)
                where(col(Post::uuid).equal(uuid).and(col(Member::uuid).equal(writerUUID)))
            }
        } catch (e: NoResultException) {
            throw PostException(PostExceptionMessage.POST_IS_NULL)
        }
    }

    override fun findOneDtoByUUID(uuid: UUID): PostInfo {
        return try {
            queryFactory.singleQuery {
                select(listOf(
                    col(Post::uuid),
                    col(Member::uuid),
                    col(Post::content),
                    col(Post::createdDate)
                ))
                from(Post::class)
                join(Post::writer)
                where(col(Post::uuid).equal(uuid))
            }
        } catch (e: NoResultException) {
            throw PostException(PostExceptionMessage.POST_IS_NULL)
        }
    }

    override fun findMyPosts(memberUUID: UUID, lastUUID: UUID?): List<PostInfo> {
        return queryFactory.listQuery {
            select(listOf(
                col(Post::uuid),
                col(Member::uuid),
                col(Post::content),
                col(Post::createdDate)
            ))
            from(Post::class)
            join(Post::writer)
            where(col(Member::uuid).equal(memberUUID))
            where(ltLastUUID(lastUUID))
            orderBy(col(Post::id).desc())
            limit(PostRepoConstant.LIMIT_PAGE)
        }
    }

    override fun findAllPosts(lastUUID: UUID?): List<PostInfo> {
        return queryFactory.listQuery {
            select(listOf(
                col(Post::uuid),
                col(Member::uuid),
                col(Post::content),
                col(Post::createdDate)
            ))
            from(Post::class)
            join(Post::writer)
            where(ltLastUUID(lastUUID))
            orderBy(col(Post::id).desc())
            limit(PostRepoConstant.LIMIT_PAGE)
        }
    }

    override fun findPostsBySomeone(someoneUUID: UUID, lastUUID: UUID?): List<PostInfo> {
        return queryFactory.listQuery {
            select(listOf(
                col(Post::uuid),
                col(Member::uuid),
                col(Post::content),
                col(Post::createdDate)
            ))
            from(Post::class)
            join(Post::writer)
            where(col(Member::uuid).equal(someoneUUID))
            where(ltLastUUID(lastUUID))
            orderBy(col(Post::id).desc())
            limit(PostRepoConstant.LIMIT_PAGE)
        }
    }

    override fun findPostsByFollowee(followeeUUID: List<UUID>, lastUUID: UUID?): List<PostInfo> {
        return queryFactory.listQuery {
            select(listOf(
                col(Post::uuid),
                col(Member::uuid),
                col(Post::content),
                col(Post::createdDate)
            ))
            from(Post::class)
            join(Post::writer)
            where(col(Member::uuid).`in`(followeeUUID))
            where(ltLastUUID(lastUUID))
            orderBy(col(Post::id).desc())
            limit(PostRepoConstant.LIMIT_PAGE)
        }
    }

    override fun findRecommendPosts(keyword: String?): List<PostInfo> {
        return queryFactory.listQuery {
            select(listOf(
                col(Post::uuid),
                col(Member::uuid),
                col(Post::content),
                col(Post::createdDate)
            ))
            from(Post::class)
            join(Post::writer)
            where(dynamicKeywordSearch(keyword))
            orderBy(col(Post::id).desc())
            limit(PostRepoConstant.LIMIT_PAGE)
            offset(PostRepoConstant.START_OFFSET)
            limit(PostRepoConstant.RECOMMEND_LIMIT_PAGE)
        }
    }

    override fun findRandomPosts(): List<PostInfo> {
        val count = queryFactory.singleQuery {
            select(count(entity(Post::class)))
            from(Post::class)
        }
        val random = Random(System.currentTimeMillis())
        val randomId = List(PostRepoConstant.RANDOM_LIST_SIZE) { (random.nextInt(Math.toIntExact(count)) + PostRepoConstant.RANDOM_START_ID).toLong() }

        return queryFactory.listQuery {
            select(listOf(
                col(Post::uuid),
                col(Member::uuid),
                col(Post::content),
                col(Post::createdDate)
            ))
            from(Post::class)
            join(Post::writer)
            where(col(Post::id).`in`(randomId))
            orderBy(col(Post::id).desc())
            limit(PostRepoConstant.LIMIT_PAGE)
        }
    }

    override fun countPostByWriter(writerUUID: UUID): Long {
        return queryFactory.singleQuery {
            select(count(entity(Post::class)))
            from(Post::class)
            join(Post::writer)
            where(col(Member::uuid).equal(writerUUID))
        }
    }

    private fun findLastId(lastUUID: UUID): Long {
        return queryFactory.singleQuery {
            select(listOf(col(Post::id)))
            from(Post::class)
            where(col(Post::uuid).equal(lastUUID))
        }
    }

    private fun <T> SpringDataCriteriaQueryDsl<T>.ltLastUUID(lastUUID: UUID?): PredicateSpec? {
        val lastId = lastUUID?.let { findLastId(it) }
        return lastUUID?.let { and(col(Post::id).lessThan(lastId!!)) }
    }

    private fun <T> SpringDataCriteriaQueryDsl<T>.dynamicKeywordSearch(keyword: String?): PredicateSpec? {
        return keyword?.let { and(col(Post::content).like("%$keyword%")) }
    }
}