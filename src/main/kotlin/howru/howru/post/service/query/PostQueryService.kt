package howru.howru.post.service.query

import howru.howru.exception.exception.SubscribeException
import howru.howru.exception.message.SubscribeExceptionMessage
import howru.howru.globalConfig.cache.constant.CacheName
import howru.howru.globalUtil.extractKeywords
import howru.howru.member.service.query.MemberQueryService
import howru.howru.post.cache.PostCache
import howru.howru.post.dto.response.PostInfo
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
    private val memberQueryService: MemberQueryService,
    private val subscribeQueryService: SubscribeQueryService
) {
    @Cacheable(cacheNames = [CacheName.POST], key = PostCache.ID_KEY)
    fun getPostById(id: Long) = postRepository.findOneDtoById(id)
    fun getMyPosts(memberUUID: UUID, lastId: Long?) = postRepository.findMyPosts(memberUUID, lastId)
    fun getAllPosts(lastId: Long?) = postRepository.findAllPosts(lastId)
    fun getPostsBySomeone(writerUUID: UUID, memberUUID: UUID, lastId: Long?): List<PostInfo> {
        val writer = memberQueryService.getMemberByUUID(writerUUID)
        return if (writer.isUnlock()) {
            postRepository.findPostsBySomeone(writerUUID, lastId)
        } else {
            takeIf { subscribeQueryService.isFollowee(writerUUID, memberUUID) }
                ?.run { postRepository.findPostsBySomeone(writerUUID, lastId) }
                ?: throw SubscribeException(SubscribeExceptionMessage.NOT_FOLLOWER, memberUUID)
        }
    }
    fun getPostsOfFollowee(followerUUID: UUID, lastId: Long?): List<PostInfo> {
        val followeeUUID = subscribeQueryService.getFollowee(followerUUID)
        return postRepository.findPostsByFollowee(followeeUUID, lastId)
    }
    fun getRecommendPosts(content: String): List<PostInfo> {
        return postRepository.findRecommendPosts(extractKeywords(content))
    }
    fun getRandomPosts() = postRepository.findRandomPosts()
    @Cacheable(cacheNames = [CacheName.POST], key = PostCache.WRITER_KEY)
    fun countPostsByWriter(writerUUID: UUID) = postRepository.countPostByWriter(writerUUID)
}