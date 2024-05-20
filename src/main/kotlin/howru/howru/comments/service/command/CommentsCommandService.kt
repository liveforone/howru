package howru.howru.comments.service.command

import howru.howru.comments.domain.Comments
import howru.howru.comments.dto.CreateComments
import howru.howru.comments.dto.RemoveComments
import howru.howru.comments.dto.UpdateComments
import howru.howru.comments.repository.CommentsRepository
import howru.howru.member.repository.MemberCustomRepository
import howru.howru.post.repository.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CommentsCommandService
    @Autowired
    constructor(
        private val commentsRepository: CommentsRepository,
        private val memberRepository: MemberCustomRepository,
        private val postRepository: PostRepository
    ) {
        fun createComments(createComments: CreateComments): Long {
            return with(createComments) {
                Comments.create(
                    writer = memberRepository.findMemberById(writerId!!),
                    post = postRepository.findPostById(postId!!),
                    content!!
                ).run { commentsRepository.save(this).id!! }
            }
        }

        fun editComment(
            id: Long,
            updateComments: UpdateComments
        ) {
            with(updateComments) {
                commentsRepository.findCommentByIdAndWriter(id, writerId!!)
                    .also { it.editContentAndState(content!!) }
            }
        }

        fun removeComment(
            id: Long,
            removeComments: RemoveComments
        ) {
            with(removeComments) {
                commentsRepository.findCommentByIdAndWriter(id, writerId!!)
                    .also { commentsRepository.delete(it) }
            }
        }
    }
