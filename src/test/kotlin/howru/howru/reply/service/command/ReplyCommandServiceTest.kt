package howru.howru.reply.service.command

import howru.howru.comments.dto.request.CreateComments
import howru.howru.comments.service.command.CommentsCommandService
import howru.howru.exception.exception.ReplyException
import howru.howru.member.dto.request.SignupRequest
import howru.howru.member.service.command.MemberCommandService
import howru.howru.post.dto.request.CreatePost
import howru.howru.post.service.command.PostCommandService
import howru.howru.reply.domain.ReplyState
import howru.howru.reply.dto.request.CreateReply
import howru.howru.reply.dto.request.DeleteReply
import howru.howru.reply.dto.update.UpdateReplyContent
import howru.howru.reply.service.query.ReplyQueryService
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.util.*

@SpringBootTest
class ReplyCommandServiceTest @Autowired constructor(
    private val entityManager: EntityManager,
    private val memberCommandService: MemberCommandService,
    private val postCommandService: PostCommandService,
    private val commentsCommandService: CommentsCommandService,
    private val replyCommandService: ReplyCommandService,
    private val replyQueryService: ReplyQueryService
) {

    private fun flushAndClear() {
        entityManager.flush()
        entityManager.clear()
    }

    private fun createPostWriter(): UUID {
        val email = "test_post_writer@gmail.com"
        val pw = "1122"
        val request = SignupRequest(email, pw)
        val uuid = memberCommandService.signupMember(request)
        flushAndClear()
        return uuid
    }

    private fun createCommentWriter(): UUID {
        val email = "test_comment_writer@gmail.com"
        val pw = "1122"
        val request = SignupRequest(email, pw)
        val uuid = memberCommandService.signupMember(request)
        flushAndClear()
        return uuid
    }

    private fun createMember(): UUID {
        val email = "test_member@gmail.com"
        val pw = "3344"
        val request = SignupRequest(email, pw)
        val uuid = memberCommandService.signupMember(request)
        flushAndClear()
        return uuid
    }

    private fun createPost(): UUID {
        val writerUUID = createPostWriter()
        val content = "test_content"
        val request = CreatePost(writerUUID, content)
        val postUUID = postCommandService.createPost(request)
        flushAndClear()
        return postUUID
    }

    private fun createComment(): UUID {
        val memberUUID = createCommentWriter()
        val postUUID = createPost()
        val content = "test_comment"
        val request = CreateComments(memberUUID, postUUID, content)
        val commentUUID = commentsCommandService.createComment(request)
        flushAndClear()
        return commentUUID
    }

    @Test @Transactional
    fun createReplyTest() {
        //given
        val memberUUID = createMember()
        val commentUUID = createComment()
        val content = "test_reply"

        //when
        val request = CreateReply(memberUUID, commentUUID, content)
        val replyUUID = replyCommandService.createReply(request)
        flushAndClear()

        //then
        Assertions.assertThat(replyQueryService.getReplyByUUID(replyUUID).content)
            .isEqualTo(content)
    }

    @Test @Transactional
    fun editReplyTest() {
        //given
        val memberUUID = createMember()
        val commentUUID = createComment()
        val content = "test_reply"
        val request = CreateReply(memberUUID, commentUUID, content)
        val replyUUID = replyCommandService.createReply(request)
        flushAndClear()

        //when
        val updatedContent = "updated reply"
        val updateRequest = UpdateReplyContent(replyUUID, memberUUID, updatedContent)
        replyCommandService.editReply(updateRequest)
        flushAndClear()

        //then
        val reply = replyQueryService.getReplyByUUID(replyUUID)
        Assertions.assertThat(reply.content).isEqualTo(updatedContent)
        Assertions.assertThat(reply.replyState).isEqualTo(ReplyState.EDITED)
    }

    @Test @Transactional
    fun deleteReplyTest() {
        //given
        val memberUUID = createMember()
        val commentUUID = createComment()
        val content = "test_reply"
        val request = CreateReply(memberUUID, commentUUID, content)
        val replyUUID = replyCommandService.createReply(request)
        flushAndClear()

        //when
        val deleteRequest = DeleteReply(replyUUID, memberUUID)
        replyCommandService.deleteReply(deleteRequest)
        flushAndClear()

        //then
        Assertions.assertThatThrownBy { replyQueryService.getReplyByUUID(replyUUID) }
            .isInstanceOf(ReplyException::class.java)
    }
}