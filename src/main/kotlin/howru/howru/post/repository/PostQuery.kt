package howru.howru.post.repository

import howru.howru.exception.exception.PostException
import howru.howru.exception.message.PostExceptionMessage
import howru.howru.member.domain.Member
import howru.howru.post.domain.Post
import howru.howru.post.dto.response.PostInfo
import howru.howru.post.repository.constant.PostRepoConstant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.util.*

@Component
class PostQuery @Autowired constructor(
    private val postRepository: PostRepository
) {
    fun findOneById(id: Long): Post {
        return postRepository.findAll {
            select(entity(Post::class))
                .from(entity(Post::class))
                .where(path(Post::id).eq(id))
        }.firstOrNull() ?: throw PostException(PostExceptionMessage.POST_IS_NULL, id)
    }

    fun findOneByIdAndWriter(id: Long, writerId: UUID): Post {
        return postRepository.findAll {
            select(entity(Post::class))
                .from(entity(Post::class), join(Post::writer))
                .where(path(Post::id).eq(id).and(path(Member::id).eq(writerId)))
        }.firstOrNull() ?: throw PostException(PostExceptionMessage.POST_IS_NULL, id)
    }

    fun findOneDtoById(id: Long): PostInfo {
        return postRepository.findAll {
            selectNew<PostInfo>(
                path(Post::id),
                path(Member::id),
                path(Post::content),
                path(Post::postState),
                path(Post::createdDatetime)
            ).from(entity(Post::class), join(Post::writer))
                .where(path(Post::id).eq(id))
        }.firstOrNull() ?: throw PostException(PostExceptionMessage.POST_IS_NULL, id)
    }

    fun findMyPosts(memberId: UUID, page: Int): List<PostInfo> {
        val pageable = PageRequest.of(page, PostRepoConstant.LIMIT_PAGE)
        return postRepository.findAll(pageable) {
            selectNew<PostInfo>(
                path(Post::id),
                path(Member::id),
                path(Post::content),
                path(Post::postState),
                path(Post::createdDatetime)
            ).from(entity(Post::class), join(Post::writer))
                .where(path(Member::id).eq(memberId))
                .orderBy(path(Post::id).desc())
        }.filterNotNull()
    }

    fun findAllPosts(page: Int): List<PostInfo> {
        val pageable = PageRequest.of(page, PostRepoConstant.LIMIT_PAGE)
        return postRepository.findAll(pageable) {
            selectNew<PostInfo>(
                path(Post::id),
                path(Member::id),
                path(Post::content),
                path(Post::postState),
                path(Post::createdDatetime)
            ).from(entity(Post::class), join(Post::writer))
                .orderBy(path(Post::id).desc())
        }.filterNotNull()
    }

    fun findPostsBySomeone(someoneId: UUID, page: Int): List<PostInfo> {
        val pageable = PageRequest.of(page, PostRepoConstant.LIMIT_PAGE)
        return postRepository.findAll(pageable) {
            selectNew<PostInfo>(
                path(Post::id),
                path(Member::id),
                path(Post::content),
                path(Post::postState),
                path(Post::createdDatetime)
            ).from(entity(Post::class), join(Post::writer))
                .where(path(Member::id).eq(someoneId))
                .orderBy(path(Post::id).desc())
        }.filterNotNull()
    }

    fun findPostsByFollowee(followeeId: List<UUID>, page: Int): List<PostInfo> {
        val pageable = PageRequest.of(page, PostRepoConstant.LIMIT_PAGE)
        return postRepository.findAll(pageable) {
            selectNew<PostInfo>(
                path(Post::id),
                path(Member::id),
                path(Post::content),
                path(Post::postState),
                path(Post::createdDatetime)
            ).from(entity(Post::class), join(Post::writer))
                .where(path(Member::id).`in`(followeeId))
                .orderBy(path(Post::id).desc())
        }.filterNotNull()
    }

    fun findRecommendPosts(keyword: String?, page: Int): List<PostInfo> {
        val pageable = PageRequest.of(page, PostRepoConstant.RECOMMEND_LIMIT_PAGE)
        return postRepository.findAll(pageable) {
            selectNew<PostInfo>(
                path(Post::id),
                path(Member::id),
                path(Post::content),
                path(Post::postState),
                path(Post::createdDatetime)
            ).from(entity(Post::class), join(Post::writer))
                .where(path(Post::content).like("%$keyword%"))
                .orderBy(path(Post::id).desc())
        }.filterNotNull()
    }
}