package howru.howru.post.repository

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import howru.howru.exception.exception.PostException
import howru.howru.exception.message.PostExceptionMessage
import howru.howru.post.domain.Post
import howru.howru.post.domain.QPost
import howru.howru.post.dto.response.PostInfo
import howru.howru.post.repository.constant.PostRepoConstant
import java.util.*

class PostCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val post: QPost = QPost.post
) : PostCustomRepository {
    override fun findPostById(id: Long): Post {
        return jpaQueryFactory.selectFrom(post)
            .where(post.id.eq(id))
            .fetchOne() ?: throw PostException(PostExceptionMessage.POST_IS_NULL, id)
    }

    override fun findPostByIdAndWriter(
        id: Long,
        writerId: UUID
    ): Post {
        return jpaQueryFactory.selectFrom(post)
            .where(post.id.eq(id).and(post.writer.id.eq(writerId)))
            .fetchOne() ?: throw PostException(PostExceptionMessage.POST_IS_NULL, id)
    }

    override fun findPostInfoById(id: Long): PostInfo {
        return jpaQueryFactory.select(
            Projections.constructor(
                PostInfo::class.java,
                post.id,
                post.writer.id,
                post.content,
                post.postState,
                post.createdDatetime
            )
        )
            .from(post)
            .where(post.id.eq(id))
            .fetchOne() ?: throw PostException(PostExceptionMessage.POST_IS_NULL, id)
    }

    override fun findPostsByWriter(
        memberId: UUID,
        lastId: Long?
    ): List<PostInfo> {
        return jpaQueryFactory.select(
            Projections.constructor(
                PostInfo::class.java,
                post.id,
                post.writer.id,
                post.content,
                post.postState,
                post.createdDatetime
            )
        )
            .from(post)
            .where(post.writer.id.eq(memberId).and(ltLastId(lastId)))
            .orderBy(post.id.desc())
            .limit(PostRepoConstant.LIMIT_PAGE)
            .fetch()
    }

    override fun findAllPosts(lastId: Long?): List<PostInfo> {
        return jpaQueryFactory.select(
            Projections.constructor(
                PostInfo::class.java,
                post.id,
                post.writer.id,
                post.content,
                post.postState,
                post.createdDatetime
            )
        )
            .from(post)
            .where(ltLastId(lastId))
            .orderBy(post.id.desc())
            .limit(PostRepoConstant.LIMIT_PAGE)
            .fetch()
    }

    override fun findPostsBySomeone(
        someoneId: UUID,
        lastId: Long?
    ): List<PostInfo> {
        return jpaQueryFactory.select(
            Projections.constructor(
                PostInfo::class.java,
                post.id,
                post.writer.id,
                post.content,
                post.postState,
                post.createdDatetime
            )
        )
            .from(post)
            .where(post.writer.id.eq(someoneId).and(ltLastId(lastId)))
            .orderBy(post.id.desc())
            .limit(PostRepoConstant.LIMIT_PAGE)
            .fetch()
    }

    override fun findPostsByFollowee(
        followeeId: List<UUID>,
        lastId: Long?
    ): List<PostInfo> {
        return jpaQueryFactory.select(
            Projections.constructor(
                PostInfo::class.java,
                post.id,
                post.writer.id,
                post.content,
                post.postState,
                post.createdDatetime
            )
        )
            .from(post)
            .where(post.writer.id.`in`(followeeId).and(ltLastId(lastId)))
            .orderBy(post.id.desc())
            .limit(PostRepoConstant.LIMIT_PAGE)
            .fetch()
    }

    override fun findRecommendPosts(
        keyword: String?,
        lastId: Long?
    ): List<PostInfo> {
        return jpaQueryFactory.select(
            Projections.constructor(
                PostInfo::class.java,
                post.id,
                post.writer.id,
                post.content,
                post.postState,
                post.createdDatetime
            )
        )
            .from(post)
            .where(post.content.startsWith(keyword).and(ltLastId(lastId)))
            .orderBy(post.id.desc())
            .limit(PostRepoConstant.RECOMMEND_LIMIT_PAGE)
            .fetch()
    }

    private fun ltLastId(lastId: Long?): BooleanExpression? = lastId?.takeIf { it > 0 }?.let { post.id.lt(it) }
}
