package howru.howru.post.service.command

import howru.howru.globalConfig.cache.constant.CacheName
import howru.howru.member.repository.MemberRepository
import howru.howru.post.cache.PostCache
import howru.howru.post.domain.Post
import howru.howru.post.dto.request.CreatePost
import howru.howru.post.dto.request.DeletePost
import howru.howru.post.dto.update.UpdatePostContent
import howru.howru.post.repository.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class PostCommandService @Autowired constructor(
    private val postRepository: PostRepository,
    private val memberRepository: MemberRepository
) {
    @CacheEvict(cacheNames = [CacheName.POST], key = PostCache.CREATE_WRITER)
    fun createPost(createPost: CreatePost): UUID {
        return with(createPost) {
            Post.create(writer = memberRepository.findOneByUUID(writerUUID!!), content!!)
                .run { postRepository.save(this).uuid }
        }
    }

    @CacheEvict(cacheNames = [CacheName.POST], key = PostCache.UPDATE_UUID)
    fun editContent(updatePostContent: UpdatePostContent) {
        with(updatePostContent) {
            postRepository.findOneByUUIDAndWriter(uuid!!, writerUUID!!)
                .also { it.editContent(content!!) }
        }
    }

    @CacheEvict(cacheNames = [CacheName.POST], key = PostCache.DELETE_UUID)
    fun deletePost(deletePost: DeletePost) {
        with(deletePost) {
            postRepository.findOneByUUIDAndWriter(uuid!!, writerUUID!!)
                .also { postRepository.delete(it) }
        }
    }
}