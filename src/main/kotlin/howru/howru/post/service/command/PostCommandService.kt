package howru.howru.post.service.command

import howru.howru.member.repository.MemberCustomRepository
import howru.howru.post.cache.PostCache
import howru.howru.post.domain.Post
import howru.howru.post.dto.CreatePost
import howru.howru.post.dto.RemovePost
import howru.howru.post.dto.UpdatePostContent
import howru.howru.post.repository.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PostCommandService
    @Autowired
    constructor(
        private val postRepository: PostRepository,
        private val memberRepository: MemberCustomRepository
    ) {
        fun createPost(createPost: CreatePost): Long {
            return with(createPost) {
                Post.create(writer = memberRepository.findMemberById(writerId!!), content!!)
                    .run { postRepository.save(this).id!! }
            }
        }

        @CacheEvict(cacheNames = [PostCache.POST_DETAIL_NAME], key = PostCache.POST_DETAIL_KEY)
        fun editPostContent(
            id: Long,
            updatePostContent: UpdatePostContent
        ) {
            with(updatePostContent) {
                postRepository.findPostByIdAndWriter(id, writerId!!)
                    .also { it.editContent(content!!) }
            }
        }

        @CacheEvict(cacheNames = [PostCache.POST_DETAIL_NAME], key = PostCache.POST_DETAIL_KEY)
        fun removePost(
            id: Long,
            removePost: RemovePost
        ) {
            with(removePost) {
                postRepository.findPostByIdAndWriter(id, writerId!!)
                    .also { postRepository.delete(it) }
            }
        }
    }
