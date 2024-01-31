package howru.howru.reply.repository

import com.linecorp.kotlinjdsl.query.spec.predicate.PredicateSpec
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.from.join
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.listQuery
import com.linecorp.kotlinjdsl.spring.data.querydsl.SpringDataCriteriaQueryDsl
import com.linecorp.kotlinjdsl.spring.data.singleQuery
import howru.howru.comments.domain.Comments
import howru.howru.exception.exception.ReplyException
import howru.howru.exception.message.ReplyExceptionMessage
import howru.howru.member.domain.Member
import howru.howru.reply.domain.Reply
import howru.howru.reply.dto.response.ReplyInfo
import howru.howru.reply.repository.constant.ReplyRepoConstant
import jakarta.persistence.NoResultException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ReplyRepositoryImpl @Autowired constructor(
    private val queryFactory: SpringDataQueryFactory
) : ReplyCustomRepository {
    override fun findOneByIdAndWriter(id: Long, writerId: UUID): Reply {
        return try {
            queryFactory.singleQuery {
                select(entity(Reply::class))
                from(Reply::class)
                join(Reply::writer)
                where(col(Reply::id).equal(id).and(col(Member::id).equal(writerId)))
            }
        } catch (e: NoResultException) {
            throw ReplyException(ReplyExceptionMessage.REPLY_IS_NULL, id)
        }
    }

    override fun findOneDtoById(id: Long): ReplyInfo {
        return try {
            queryFactory.singleQuery {
                select(listOf(
                    col(Reply::id),
                    col(Member::id),
                    col(Comments::id),
                    col(Reply::content),
                    col(Reply::replyState),
                    col(Reply::createdDatetime)
                ))
                from(Reply::class)
                join(Reply::writer)
                join(Reply::comment)
                where(col(Reply::id).equal(id))
            }
        } catch (e: NoResultException) {
            throw ReplyException(ReplyExceptionMessage.REPLY_IS_NULL, id)
        }
    }

    override fun findRepliesByWriter(writerId: UUID, lastId: Long?): List<ReplyInfo> {
        return queryFactory.listQuery {
            select(listOf(
                col(Reply::id),
                col(Member::id),
                col(Comments::id),
                col(Reply::content),
                col(Reply::replyState),
                col(Reply::createdDatetime)
            ))
            from(Reply::class)
            join(Reply::writer)
            join(Reply::comment)
            where(col(Member::id).equal(writerId))
            where(ltLastId(lastId))
            orderBy(col(Reply::id).desc())
            limit(ReplyRepoConstant.LIMIT_PAGE)
        }
    }

    override fun findRepliesByComment(commentId: Long, lastId: Long?): List<ReplyInfo> {
        return queryFactory.listQuery {
            select(listOf(
                col(Reply::id),
                col(Member::id),
                col(Comments::id),
                col(Reply::content),
                col(Reply::replyState),
                col(Reply::createdDatetime)
            ))
            from(Reply::class)
            join(Reply::writer)
            join(Reply::comment)
            where(col(Comments::id).equal(commentId))
            where(ltLastId(lastId))
            orderBy(col(Reply::id).desc())
            limit(ReplyRepoConstant.LIMIT_PAGE)
        }
    }

    private fun <T> SpringDataCriteriaQueryDsl<T>.ltLastId(lastId: Long?): PredicateSpec? {
        return lastId?.let { and(col(Reply::id).lessThan(it)) }
    }
}