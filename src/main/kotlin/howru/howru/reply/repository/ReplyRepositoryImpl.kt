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
    override fun findOneByUUIDAndWriter(uuid: UUID, writerUUID: UUID): Reply {
        return try {
            queryFactory.singleQuery {
                select(entity(Reply::class))
                from(Reply::class)
                join(Reply::writer)
                where(col(Reply::uuid).equal(uuid).and(col(Member::uuid).equal(writerUUID)))
            }
        } catch (e: NoResultException) {
            throw ReplyException(ReplyExceptionMessage.REPLY_IS_NULL)
        }
    }

    override fun findOneDtoByUUID(uuid: UUID): ReplyInfo {
        return try {
            queryFactory.singleQuery {
                select(listOf(
                    col(Reply::uuid),
                    col(Member::uuid),
                    col(Comments::uuid),
                    col(Reply::content),
                    col(Reply::replyState),
                    col(Reply::createdDate)
                ))
                from(Reply::class)
                join(Reply::writer)
                join(Reply::comment)
                where(col(Reply::uuid).equal(uuid))
            }
        } catch (e: NoResultException) {
            throw ReplyException(ReplyExceptionMessage.REPLY_IS_NULL)
        }
    }

    override fun findRepliesByWriter(writerUUID: UUID, lastUUID: UUID?): List<ReplyInfo> {
        return queryFactory.listQuery {
            select(listOf(
                col(Reply::uuid),
                col(Member::uuid),
                col(Comments::uuid),
                col(Reply::content),
                col(Reply::replyState),
                col(Reply::createdDate)
            ))
            from(Reply::class)
            join(Reply::writer)
            join(Reply::comment)
            where(col(Member::uuid).equal(writerUUID))
            where(ltLastUUID(lastUUID))
            orderBy(col(Reply::uuid).desc())
            limit(ReplyRepoConstant.LIMIT_PAGE)
        }
    }

    override fun findRepliesByComment(commentUUID: UUID, lastUUID: UUID?): List<ReplyInfo> {
        return queryFactory.listQuery {
            select(listOf(
                col(Reply::uuid),
                col(Member::uuid),
                col(Comments::uuid),
                col(Reply::content),
                col(Reply::replyState),
                col(Reply::createdDate)
            ))
            from(Reply::class)
            join(Reply::writer)
            join(Reply::comment)
            where(col(Comments::uuid).equal(commentUUID))
            where(ltLastUUID(lastUUID))
            orderBy(col(Reply::uuid).desc())
            limit(ReplyRepoConstant.LIMIT_PAGE)
        }
    }

    private fun findLastId(lastUUID: UUID): Long {
        return queryFactory.singleQuery {
            select(listOf(col(Reply::id)))
            from(Reply::class)
            where(col(Reply::uuid).equal(lastUUID))
        }
    }

    private fun <T> SpringDataCriteriaQueryDsl<T>.ltLastUUID(lastUUID: UUID?): PredicateSpec? {
        return lastUUID?.let { and(col(Reply::id).lessThan(findLastId(it))) }
    }
}