package howru.howru.post.service.query

import howru.howru.exception.exception.SubscribeException
import howru.howru.exception.message.SubscribeExceptionMessage
import howru.howru.globalConfig.cache.constant.CacheName
import howru.howru.globalUtil.extractKeywords
import howru.howru.logger
import howru.howru.member.service.query.MemberQueryService
import howru.howru.post.cache.PostCache
import howru.howru.post.dto.response.PostInfo
import howru.howru.post.log.PostServiceLog
import howru.howru.post.repository.PostQuery
import howru.howru.post.repository.PostRepository
import howru.howru.subscribe.service.query.SubscribeQueryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class PostQueryService @Autowired constructor(
    private val postRepository: PostRepository,
    private val postQuery: PostQuery,
    private val memberQueryService: MemberQueryService,
    private val subscribeQueryService: SubscribeQueryService
) {
    @Cacheable(cacheNames = [CacheName.POST], key = PostCache.ID_KEY)
    fun getPostById(id: Long) = postQuery.findOneDtoById(id)
    fun getMyPosts(memberId: UUID, page: Int) = postQuery.findMyPosts(memberId, page)
    fun getAllPosts(page: Int) = postQuery.findAllPosts(page)
    fun getPostsBySomeone(writerId: UUID, memberId: UUID, page: Int): List<PostInfo> {
        val writer = memberQueryService.getMemberById(writerId)
        return if (writer.isUnlock()) {
            postQuery.findPostsBySomeone(writerId, page)
        } else {
            takeIf { subscribeQueryService.isFollowee(writerId, memberId) }
                ?.run { postQuery.findPostsBySomeone(writerId, page) }
                ?: run { logger().warn(PostServiceLog.NOT_FOLLOWER + writerId); throw SubscribeException(SubscribeExceptionMessage.NOT_FOLLOWER, memberId) }
        }
    }
    fun getPostsOfFollowee(followerId: UUID, page: Int): List<PostInfo> {
        val followeeId = subscribeQueryService.getFollowees(followerId)
        return postQuery.findPostsByFollowee(followeeId, page)
    }
    fun getRecommendPosts(content: String, page: Int): List<PostInfo> {
        return postQuery.findRecommendPosts(extractKeywords(content), page)
    }
    fun getRandomPosts() = postRepository.findRandomPosts()
    @Cacheable(cacheNames = [CacheName.POST], key = PostCache.WRITER_KEY)
    fun getCountOfPostsByWriter(writerId: UUID) = postRepository.countOfPostByWriter(writerId)
}