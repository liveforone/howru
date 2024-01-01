package howru.howru.post.repository

import com.linecorp.kotlinjdsl.query.spec.predicate.PredicateSpec
import com.linecorp.kotlinjdsl.querydsl.expression.col
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

@Repository
class PostRepositoryImpl @Autowired constructor(
    private val queryFactory: SpringDataQueryFactory
) : PostCustomRepository {
    override fun findOneById(id: Long): Post {
        return try {
            queryFactory.singleQuery {
                select(entity(Post::class))
                from(Post::class)
                where(col(Post::id).equal(id))
            }
        } catch (e: NoResultException) {
            throw PostException(PostExceptionMessage.POST_IS_NULL, id)
        }
    }

    override fun findOneByIdAndWriter(id: Long, writerUUID: UUID): Post {
        return try {
            queryFactory.singleQuery {
                select(entity(Post::class))
                from(Post::class)
                join(Post::writer)
                where(col(Post::id).equal(id).and(col(Member::uuid).equal(writerUUID)))
            }
        } catch (e: NoResultException) {
            throw PostException(PostExceptionMessage.POST_IS_NULL, id)
        }
    }

    override fun findOneDtoById(id: Long): PostInfo {
        return try {
            queryFactory.singleQuery {
                select(listOf(
                    col(Post::id),
                    col(Member::uuid),
                    col(Post::content),
                    col(Post::postState),
                    col(Post::createdDatetime)
                ))
                from(Post::class)
                join(Post::writer)
                where(col(Post::id).equal(id))
            }
        } catch (e: NoResultException) {
            throw PostException(PostExceptionMessage.POST_IS_NULL, id)
        }
    }

    override fun findMyPosts(memberUUID: UUID, lastId: Long?): List<PostInfo> {
        return queryFactory.listQuery {
            select(listOf(
                col(Post::id),
                col(Member::uuid),
                col(Post::content),
                col(Post::postState),
                col(Post::createdDatetime)
            ))
            from(Post::class)
            join(Post::writer)
            where(col(Member::uuid).equal(memberUUID))
            where(ltLastId(lastId))
            orderBy(col(Post::id).desc())
            limit(PostRepoConstant.LIMIT_PAGE)
        }
    }

    override fun findAllPosts(lastId: Long?): List<PostInfo> {
        return queryFactory.listQuery {
            select(listOf(
                col(Post::id),
                col(Member::uuid),
                col(Post::content),
                col(Post::postState),
                col(Post::createdDatetime)
            ))
            from(Post::class)
            join(Post::writer)
            where(ltLastId(lastId))
            orderBy(col(Post::id).desc())
            limit(PostRepoConstant.LIMIT_PAGE)
        }
    }

    override fun findPostsBySomeone(someoneUUID: UUID, lastId: Long?): List<PostInfo> {
        return queryFactory.listQuery {
            select(listOf(
                col(Post::id),
                col(Member::uuid),
                col(Post::content),
                col(Post::postState),
                col(Post::createdDatetime)
            ))
            from(Post::class)
            join(Post::writer)
            where(col(Member::uuid).equal(someoneUUID))
            where(ltLastId(lastId))
            orderBy(col(Post::id).desc())
            limit(PostRepoConstant.LIMIT_PAGE)
        }
    }

    override fun findPostsByFollowee(followeeUUID: List<UUID>, lastId: Long?): List<PostInfo> {
        return queryFactory.listQuery {
            select(listOf(
                col(Post::id),
                col(Member::uuid),
                col(Post::content),
                col(Post::postState),
                col(Post::createdDatetime)
            ))
            from(Post::class)
            join(Post::writer)
            where(col(Member::uuid).`in`(followeeUUID))
            where(ltLastId(lastId))
            orderBy(col(Post::id).desc())
            limit(PostRepoConstant.LIMIT_PAGE)
        }
    }

    override fun findRecommendPosts(keyword: String?): List<PostInfo> {
        return queryFactory.listQuery {
            select(listOf(
                col(Post::id),
                col(Member::uuid),
                col(Post::content),
                col(Post::postState),
                col(Post::createdDatetime)
            ))
            from(Post::class)
            join(Post::writer)
            where(dynamicKeywordSearch(keyword))
            orderBy(col(Post::id).desc())
            limit(PostRepoConstant.NEWEST_LIMIT_PAGE)
            offset(PostRepoConstant.START_OFFSET)
            limit(PostRepoConstant.RECOMMEND_LIMIT_PAGE)
        }
    }

    override fun countOfPostByWriter(writerUUID: UUID): Long {
        return queryFactory.singleQuery {
            select(count(entity(Post::class)))
            from(Post::class)
            join(Post::writer)
            where(col(Member::uuid).equal(writerUUID))
        }
    }

    private fun <T> SpringDataCriteriaQueryDsl<T>.ltLastId(lastId: Long?): PredicateSpec? {
        return lastId?.let { and(col(Post::id).lessThan(it)) }
    }

    private fun <T> SpringDataCriteriaQueryDsl<T>.dynamicKeywordSearch(keyword: String?): PredicateSpec? {
        return keyword?.let { and(col(Post::content).like("%$keyword%")) }
    }
}