package howru.howru.post.repository

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import howru.howru.post.exception.PostException
import howru.howru.post.exception.PostExceptionMessage
import howru.howru.global.util.findLastIdOrDefault
import howru.howru.global.util.ltLastId
import howru.howru.post.domain.Post
import howru.howru.post.domain.QPost
import howru.howru.post.dto.response.PostInfo
import howru.howru.post.dto.response.PostPage
import howru.howru.post.repository.constant.PostRepoConstant
import java.util.*

class PostCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val post: QPost = QPost.post
) : PostCustomRepository {
    override fun findPostById(id: Long): Post =
        jpaQueryFactory
            .selectFrom(post)
            .where(post.id.eq(id))
            .fetchOne() ?: throw PostException(PostExceptionMessage.POST_IS_NULL, id)

    override fun findPostByIdAndWriter(
        id: Long,
        writerId: UUID
    ): Post =
        jpaQueryFactory
            .selectFrom(post)
            .where(post.id.eq(id).and(post.writer.id.eq(writerId)))
            .fetchOne() ?: throw PostException(PostExceptionMessage.POST_IS_NULL, id)

    private val postInfoField =
        Projections.constructor(
            PostInfo::class.java,
            post.id,
            post.writer.id,
            post.content,
            post.postState,
            post.createdDatetime
        )

    override fun findPostInfoById(id: Long): PostInfo =
        jpaQueryFactory
            .select(postInfoField)
            .from(post)
            .where(post.id.eq(id))
            .fetchOne() ?: throw PostException(PostExceptionMessage.POST_IS_NULL, id)

    override fun findPostsByMember(
        memberId: UUID,
        lastId: Long?
    ): PostPage {
        val postInfoList =
            jpaQueryFactory
                .select(postInfoField)
                .from(post)
                .where(
                    post.writer.id
                        .eq(memberId)
                        .and(ltLastId(lastId, post) { it.id })
                ).orderBy(post.id.desc())
                .limit(PostRepoConstant.LIMIT_PAGE)
                .fetch()

        return PostPage(postInfoList, findLastIdOrDefault(postInfoList) { it.id })
    }

    override fun findAllPosts(lastId: Long?): PostPage {
        val postInfoList =
            jpaQueryFactory
                .select(postInfoField)
                .from(post)
                .where(ltLastId(lastId, post) { it.id })
                .orderBy(post.id.desc())
                .limit(PostRepoConstant.LIMIT_PAGE)
                .fetch()

        return PostPage(postInfoList, findLastIdOrDefault(postInfoList) { it.id })
    }

    override fun findPostsByFolloweeIds(
        followeeId: List<UUID>,
        lastId: Long?
    ): PostPage {
        val postInfoList =
            jpaQueryFactory
                .select(postInfoField)
                .from(post)
                .where(
                    post.writer.id
                        .`in`(followeeId)
                        .and(ltLastId(lastId, post) { it.id })
                ).orderBy(post.id.desc())
                .limit(PostRepoConstant.LIMIT_PAGE)
                .fetch()

        return PostPage(postInfoList, findLastIdOrDefault(postInfoList) { it.id })
    }

    override fun findRecommendPosts(
        keyword: String?,
        lastId: Long?
    ): PostPage {
        val postInfoList =
            jpaQueryFactory
                .select(postInfoField)
                .from(post)
                .where(post.content.startsWith(keyword).and(ltLastId(lastId, post) { it.id }))
                .orderBy(post.id.desc())
                .limit(PostRepoConstant.RECOMMEND_LIMIT_PAGE)
                .fetch()

        return PostPage(postInfoList, findLastIdOrDefault(postInfoList) { it.id })
    }
}
