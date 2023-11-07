package howru.howru.post.service.command

import howru.howru.globalConfig.cache.constant.CacheName
import howru.howru.member.repository.MemberQuery
import howru.howru.post.cache.PostCache
import howru.howru.post.domain.Post
import howru.howru.post.dto.request.CreatePost
import howru.howru.post.dto.request.RemovePost
import howru.howru.post.dto.update.UpdatePostContent
import howru.howru.post.repository.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PostCommandService @Autowired constructor(
    private val postRepository: PostRepository,
    private val memberQuery: MemberQuery
) {
    @CacheEvict(cacheNames = [CacheName.POST], key = PostCache.CREATE_WRITER)
    fun createPost(createPost: CreatePost): Long {
        return with(createPost) {
            Post.create(writer = memberQuery.findOneByUUID(writerUUID!!), content!!)
                .run { postRepository.save(this).id!! }
        }
    }

    @CacheEvict(cacheNames = [CacheName.POST], key = PostCache.UPDATE_ID)
    fun editContent(updatePostContent: UpdatePostContent) {
        with(updatePostContent) {
            postRepository.findOneByIdAndWriter(id!!, writerUUID!!)
                .also { it.editContent(content!!) }
        }
    }

    @CacheEvict(cacheNames = [CacheName.POST], key = PostCache.DELETE_ID)
    fun removePost(removePost: RemovePost) {
        with(removePost) {
            postRepository.findOneByIdAndWriter(id!!, writerUUID!!)
                .also { postRepository.delete(it) }
        }
    }
}