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
    override fun findOneById(id: Long): Comments {
        return try {
            queryFactory.singleQuery {
                select(entity(Comments::class))
                from(Comments::class)
                join(Comments::writer)
                where(col(Comments::id).equal(id))
            }
        } catch (e: NoResultException) {
            throw CommentsException(CommentsExceptionMessage.COMMENTS_IS_NULL, id)
        }
    }

    override fun findOneByIdAndWriter(id: Long, writerUUID: UUID): Comments {
        return try {
            queryFactory.singleQuery {
                select(entity(Comments::class))
                from(Comments::class)
                join(Comments::writer)
                where(col(Comments::id).equal(id).and(col(Member::uuid).equal(writerUUID)))
            }
        } catch (e: NoResultException) {
            throw CommentsException(CommentsExceptionMessage.COMMENTS_IS_NULL, id)
        }
    }

    override fun findOneDtoById(id: Long): CommentsInfo {
        return try {
            queryFactory.singleQuery {
                select(listOf(
                    col(Comments::id),
                    col(Member::uuid),
                    col(Post::id),
                    col(Comments::content),
                    col(Comments::commentsState),
                    col(Comments::createdDatetime)
                ))
                from(Comments::class)
                join(Comments::writer)
                join(Comments::post)
                where(col(Comments::id).equal(id))
            }
        } catch (e: NoResultException) {
            throw CommentsException(CommentsExceptionMessage.COMMENTS_IS_NULL, id)
        }
    }

    override fun findCommentsByWriter(writerUUID: UUID, lastId: Long?): List<CommentsInfo> {
        return queryFactory.listQuery {
            select(listOf(
                col(Comments::id),
                col(Member::uuid),
                col(Post::id),
                col(Comments::content),
                col(Comments::commentsState),
                col(Comments::createdDatetime)
            ))
            from(Comments::class)
            join(Comments::writer)
            join(Comments::post)
            where(col(Member::uuid).equal(writerUUID))
            where(ltLastId(lastId))
            orderBy(col(Comments::id).desc())
            limit(CommentsRepoConstant.LIMIT_PAGE)
        }
    }

    override fun findCommentsByPost(postId: Long, lastId: Long?): List<CommentsInfo> {
        return queryFactory.listQuery {
            select(listOf(
                col(Comments::id),
                col(Member::uuid),
                col(Post::id),
                col(Comments::content),
                col(Comments::commentsState),
                col(Comments::createdDatetime)
            ))
            from(Comments::class)
            join(Comments::writer)
            join(Comments::post)
            where(col(Post::id).equal(postId))
            where(ltLastId(lastId))
            orderBy(col(Comments::id).desc())
            limit(CommentsRepoConstant.LIMIT_PAGE)
        }
    }

    private fun <T> SpringDataCriteriaQueryDsl<T>.ltLastId(lastId: Long?): PredicateSpec? {
        return lastId?.let { and(col(Comments::id).lessThan(it)) }
    }
}