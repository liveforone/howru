package howru.howru.comments.service.command

import howru.howru.comments.domain.Comments
import howru.howru.comments.dto.request.CreateComments
import howru.howru.comments.dto.request.RemoveComments
import howru.howru.comments.dto.update.UpdateCommentsContent
import howru.howru.comments.repository.CommentsRepository
import howru.howru.member.repository.MemberRepository
import howru.howru.post.repository.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CommentsCommandService @Autowired constructor(
    private val commentsRepository: CommentsRepository,
    private val memberRepository: MemberRepository,
    private val postRepository: PostRepository
) {
    fun createComment(createComments: CreateComments): Long {
        return with(createComments) {
            Comments.create(
                writer = memberRepository.findOneByUUID(writerUUID!!),
                post = postRepository.findOneById(postId!!),
                content!!
            ).run { commentsRepository.save(this).id!! }
        }
    }

    fun editComment(updateCommentsContent: UpdateCommentsContent) {
        with(updateCommentsContent) {
            commentsRepository.findOneByIdAndWriter(id!!, writerUUID!!)
                .also { it.editContent(content!!) }
        }
    }

    fun removeComment(removeComments: RemoveComments) {
        with(removeComments) {
            commentsRepository.findOneByIdAndWriter(id!!, writerUUID!!)
                .also { commentsRepository.delete(it) }
        }
    }
}