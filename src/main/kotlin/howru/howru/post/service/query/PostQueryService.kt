package howru.howru.post.service.query

import howru.howru.global.util.extractKeywords
import howru.howru.post.cache.PostCache
import howru.howru.post.dto.response.PostPage
import howru.howru.post.repository.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class PostQueryService
    @Autowired
    constructor(
        private val postRepository: PostRepository
    ) {
        @Cacheable(cacheNames = [PostCache.POST_DETAIL_NAME], key = PostCache.POST_DETAIL_KEY)
        fun getPostById(id: Long) = postRepository.findPostInfoById(id)

        fun getAllPosts(lastId: Long?) = postRepository.findAllPosts(lastId)

        fun getPostsByMember(
            memberId: UUID,
            lastId: Long?
        ) = postRepository.findPostsByMember(memberId, lastId)

        fun getPostsByFolloweeIds(
            followeeIds: List<UUID>,
            lastId: Long?
        ) = postRepository.findPostsByFolloweeIds(followeeIds, lastId)

        fun getRecommendPosts(
            content: String,
            lastId: Long?
        ): PostPage {
            return postRepository.findRecommendPosts(extractKeywords(content), lastId)
        }

        fun getRandomPosts() = postRepository.findRandomPosts()

        fun getCountOfPostByMember(memberId: UUID) = postRepository.countOfPostByMember(memberId)
    }
