package howru.howru.comments.service.command

import howru.howru.comments.domain.Comments
import howru.howru.comments.dto.request.CreateComments
import howru.howru.comments.dto.request.DeleteComments
import howru.howru.comments.dto.update.UpdateCommentsContent
import howru.howru.comments.repository.CommentsRepository
import howru.howru.member.repository.MemberRepository
import howru.howru.post.repository.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class CommentsCommandService @Autowired constructor(
    private val commentsRepository: CommentsRepository,
    private val memberRepository: MemberRepository,
    private val postRepository: PostRepository
) {
    fun createComment(createComments: CreateComments): UUID {
        return with(createComments) {
            Comments.create(
                writer = memberRepository.findOneByUUID(writerUUID!!),
                post = postRepository.findOneByUUID(postUUID!!),
                content!!
            ).run { commentsRepository.save(this).uuid }
        }
    }

    fun editComment(updateCommentsContent: UpdateCommentsContent) {
        with(updateCommentsContent) {
            commentsRepository.findOneByUUIDAndWriter(uuid!!, writerUUID!!)
                .also { it.editContent(content!!) }
        }
    }

    fun deleteComment(deleteComments: DeleteComments) {
        with(deleteComments) {
            commentsRepository.findOneByUUIDAndWriter(uuid!!, writerUUID!!)
                .also { commentsRepository.delete(it) }
        }
    }
}