package howru.howru.post.service.query

import howru.howru.exception.exception.PostException
import howru.howru.exception.message.PostExceptionMessage
import howru.howru.globalUtil.extractKeywords
import howru.howru.member.service.query.MemberQueryService
import howru.howru.post.dto.response.PostInfo
import howru.howru.post.repository.PostRepository
import howru.howru.subscribe.service.query.SubscribeQueryService
import org.springframework.beans.factory.annotation.Autowired
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
    fun getPostByUUID(uuid: UUID) = postRepository.findOneDtoByUUID(uuid)
    fun getMyPosts(memberUUID: UUID, lastUUID: UUID?) = postRepository.findMyPosts(memberUUID, lastUUID)
    fun getAllPosts(lastUUID: UUID?) = postRepository.findAllPosts(lastUUID)
    fun getPostsBySomeone(writerUUID: UUID, memberUUID: UUID, lastUUID: UUID?): List<PostInfo> {
        val writer = memberQueryService.getMemberByUUID(writerUUID)
        return if (writer.isUnlock()) {
            postRepository.findPostsBySomeone(writerUUID, lastUUID)
        } else {
            takeIf { subscribeQueryService.isFollowee(writerUUID, memberUUID) }
                ?.run { postRepository.findPostsBySomeone(writerUUID, lastUUID) }
                ?: throw PostException(PostExceptionMessage.NOT_FOLLOWER)
        }
    }
    fun getPostsOfFollowee(followerUUID: UUID, lastUUID: UUID?): List<PostInfo> {
        val followeeUUID = subscribeQueryService.getFollowee(followerUUID)
        return postRepository.findPostsByFollowee(followeeUUID, lastUUID)
    }
    fun getRecommendPosts(content: String): List<PostInfo> {
        return postRepository.findRecommendPosts(extractKeywords(content))
    }
    fun countPostsByWriter(writerUUID: UUID) = postRepository.countPostByWriter(writerUUID)
}