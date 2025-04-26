package howru.howru.post.service.command

import howru.howru.global.config.redis.RedisRepository
import howru.howru.member.repository.MemberCustomRepository
import howru.howru.post.cache.PostCacheKey
import howru.howru.post.domain.Post
import howru.howru.post.dto.request.CreatePost
import howru.howru.post.dto.request.RemovePost
import howru.howru.post.dto.request.UpdatePostContent
import howru.howru.post.repository.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PostCommandService
    @Autowired
    constructor(
        private val redisRepository: RedisRepository,
        private val postRepository: PostRepository,
        private val memberRepository: MemberCustomRepository
    ) {
        fun createPost(createPost: CreatePost): Long =
            with(createPost) {
                Post
                    .create(writer = memberRepository.findMemberById(writerId!!), content!!)
                    .run { postRepository.save(this).id!! }
            }

        fun editPostContent(
            id: Long,
            updatePostContent: UpdatePostContent
        ) {
            with(updatePostContent) {
                postRepository
                    .findPostByIdAndWriter(id, writerId!!)
                    .also {
                        it.editContent(content!!)
                        redisRepository.delete(PostCacheKey.POST_DETAIL + id)
                    }
            }
        }

        fun removePost(
            id: Long,
            removePost: RemovePost
        ) {
            with(removePost) {
                postRepository
                    .findPostByIdAndWriter(id, writerId!!)
                    .also {
                        postRepository.delete(it)
                        redisRepository.delete(PostCacheKey.POST_DETAIL + id)
                    }
            }
        }
    }
