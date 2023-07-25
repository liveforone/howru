package howru.howru.comments.repository

import com.linecorp.kotlinjdsl.query.spec.predicate.PredicateSpec
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.from.join
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.listQuery
import com.linecorp.kotlinjdsl.spring.data.querydsl.SpringDataCriteriaQueryDsl
import com.linecorp.kotlinjdsl.spring.data.singleQuery
import howru.howru.comments.domain.Comments
import howru.howru.comments.dto.response.CommentsInfo
import howru.howru.comments.repository.constant.CommentsRepoConstant
import howru.howru.exception.exception.CommentsException
import howru.howru.exception.message.CommentsExceptionMessage
import howru.howru.member.domain.Member
import howru.howru.post.domain.Post
import jakarta.persistence.NoResultException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CommentsRepositoryImpl @Autowired constructor(
    private val queryFactory: SpringDataQueryFactory
) : CommentsCustomRepository {
    override fun findOneByUUID(uuid: UUID, writerUUID: UUID): Comments {
        return try {
            queryFactory.singleQuery {
                select(entity(Comments::class))
                from(Comments::class)
                join(Comments::writer)
                where(col(Comments::uuid).equal(uuid).and(col(Member::uuid).equal(writerUUID)))
            }
        } catch (e: NoResultException) {
            throw CommentsException(CommentsExceptionMessage.COMMENTS_IS_NULL)
        }
    }

    override fun findOneDtoByUUID(uuid: UUID): CommentsInfo {
        return try {
            queryFactory.singleQuery {
                select(listOf(
                    col(Comments::uuid),
                    col(Member::uuid),
                    col(Post::uuid),
                    col(Comments::content),
                    col(Comments::commentsState),
                    col(Comments::createdDate)
                ))
                from(Comments::class)
                join(Comments::writer)
                join(Comments::post)
                where(col(Comments::uuid).equal(uuid))
            }
        } catch (e: NoResultException) {
            throw CommentsException(CommentsExceptionMessage.COMMENTS_IS_NULL)
        }
    }

    override fun findCommentsByWriter(writerUUID: UUID, lastUUID: UUID?): List<CommentsInfo> {
        return queryFactory.listQuery {
            select(listOf(
                col(Comments::uuid),
                col(Member::uuid),
                col(Post::uuid),
                col(Comments::content),
                col(Comments::commentsState),
                col(Comments::createdDate)
            ))
            from(Comments::class)
            join(Comments::writer)
            join(Comments::post)
            where(col(Member::uuid).equal(writerUUID))
            where(ltLastUUID(lastUUID))
            orderBy(col(Comments::id).desc())
            limit(CommentsRepoConstant.LIMIT_PAGE)
        }
    }

    override fun findCommentsByPost(postUUID: UUID, lastUUID: UUID?): List<CommentsInfo> {
        return queryFactory.listQuery {
            select(listOf(
                col(Comments::uuid),
                col(Member::uuid),
                col(Post::uuid),
                col(Comments::content),
                col(Comments::commentsState),
                col(Comments::createdDate)
            ))
            from(Comments::class)
            join(Comments::writer)
            join(Comments::post)
            where(col(Post::uuid).equal(postUUID))
            where(ltLastUUID(lastUUID))
            orderBy(col(Comments::id).desc())
            limit(CommentsRepoConstant.LIMIT_PAGE)
        }
    }

    private fun findLastId(lastUUID: UUID): Long {
        return queryFactory.singleQuery {
            select(listOf(col(Comments::id)))
            from(Comments::class)
            where(col(Comments::uuid).equal(lastUUID))
        }
    }

    private fun <T> SpringDataCriteriaQueryDsl<T>.ltLastUUID(lastUUID: UUID?): PredicateSpec? {
        return lastUUID?.let { and(col(Comments::id).lessThan(findLastId(it))) }
    }
}