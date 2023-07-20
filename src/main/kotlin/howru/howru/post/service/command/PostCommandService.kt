package howru.howru.post.service.command

import howru.howru.member.repository.MemberRepository
import howru.howru.post.domain.Post
import howru.howru.post.dto.request.CreatePost
import howru.howru.post.dto.request.DeletePost
import howru.howru.post.dto.update.UpdateContent
import howru.howru.post.repository.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class PostCommandService @Autowired constructor(
    private val postRepository: PostRepository,
    private val memberRepository: MemberRepository
) {
    fun createPost(createPost: CreatePost): UUID {
        return with(createPost) {
            Post.create(writer = memberRepository.findOneByUUID(writerUUID!!), content!!)
                .run { postRepository.save(this).uuid }
        }
    }

    fun editContent(updateContent: UpdateContent) {
        with(updateContent) {
            postRepository.findOneByUUIDAndWriter(uuid!!, writerUUID!!)
                .also { it.editContent(content!!) }
        }
    }

    fun deletePost(deletePost: DeletePost) {
        with(deletePost) {
            postRepository.findOneByUUIDAndWriter(uuid!!, writerUUID!!)
                .also { postRepository.delete(it) }
        }
    }
}