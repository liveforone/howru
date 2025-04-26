package howru.howru.post.service.query

import howru.howru.global.config.redis.RedisRepository
import howru.howru.global.config.redis.RedisTimeOut
import howru.howru.global.config.redis.constant.CacheTTL
import howru.howru.global.util.extractKeywords
import howru.howru.post.cache.PostCacheKey
import howru.howru.post.dto.response.PostInfo
import howru.howru.post.dto.response.PostPage
import howru.howru.post.repository.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import java.util.concurrent.TimeUnit

@Service
@Transactional(readOnly = true)
class PostQueryService
    @Autowired
    constructor(
        private val redisRepository: RedisRepository,
        private val postRepository: PostRepository
    ) {
        fun getPostById(id: Long): PostInfo =
            redisRepository.getOrLoad(
                PostCacheKey.POST_DETAIL + id,
                PostInfo::class.java,
                findDataFromDB = { postRepository.findPostInfoById(id) },
                RedisTimeOut(CacheTTL.TEN, TimeUnit.MINUTES)
            )

        fun getAllPosts(lastId: Long?) = postRepository.findAllPosts(lastId)

        /**
         * This method does not check the following relationship between members.
         * If you need to check the following relationship, use the 'getPostOfOtherMember()' function
         */
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
        ): PostPage = postRepository.findRecommendPosts(extractKeywords(content), lastId)

        fun getRandomPosts() = postRepository.findRandomPosts()

        fun getCountOfPostByMember(memberId: UUID) = postRepository.countOfPostByMember(memberId)
    }
