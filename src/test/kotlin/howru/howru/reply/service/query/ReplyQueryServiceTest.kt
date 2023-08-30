package howru.howru.reply.service.query

import howru.howru.comments.dto.request.CreateComments
import howru.howru.comments.service.command.CommentsCommandService
import howru.howru.member.dto.request.LoginRequest
import howru.howru.member.dto.request.SignupRequest
import howru.howru.member.service.command.MemberCommandService
import howru.howru.post.dto.request.CreatePost
import howru.howru.post.service.command.PostCommandService
import howru.howru.reply.dto.request.CreateReply
import howru.howru.reply.service.command.ReplyCommandService
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.util.*

@SpringBootTest
class ReplyQueryServiceTest @Autowired constructor(
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
        val nickName = "pWriter"
        val request = SignupRequest(email, pw, nickName)
        memberCommandService.signupMember(request)
        flushAndClear()
        val loginRequest = LoginRequest(email, pw)
        return memberCommandService.login(loginRequest).uuid
    }

    private fun createCommentWriter(): UUID {
        val email = "test_comment_writer@gmail.com"
        val pw = "1122"
        val nickName = "cWriter"
        val request = SignupRequest(email, pw, nickName)
        memberCommandService.signupMember(request)
        flushAndClear()
        val loginRequest = LoginRequest(email, pw)
        return memberCommandService.login(loginRequest).uuid
    }

    private fun createMember(): UUID {
        val email = "test_member@gmail.com"
        val pw = "3344"
        val nickName = "member"
        val request = SignupRequest(email, pw, nickName)
        memberCommandService.signupMember(request)
        flushAndClear()
        val loginRequest = LoginRequest(email, pw)
        return memberCommandService.login(loginRequest).uuid
    }

    private fun createPost(): Long {
        val writerUUID = createPostWriter()
        val content = "test_content"
        val request = CreatePost(writerUUID, content)
        val postId = postCommandService.createPost(request)
        flushAndClear()
        return postId
    }

    private fun createComment(): Long {
        val memberUUID = createCommentWriter()
        val postId = createPost()
        val content = "test_comment"
        val request = CreateComments(memberUUID, postId, content)
        val commentId = commentsCommandService.createComment(request)
        flushAndClear()
        return commentId
    }

    @Test @Transactional
    fun getReplyByIdTest() {
        //given
        val memberUUID = createMember()
        val commentId = createComment()
        val content = "test_reply"
        val request = CreateReply(memberUUID, commentId, content)
        val replyId = replyCommandService.createReply(request)
        flushAndClear()

        //when
        val reply = replyQueryService.getReplyById(replyId)

        //then
        Assertions.assertThat(reply).isNotNull
    }

    @Test @Transactional
    fun getRepliesByWriterTest() {
        //given
        val memberUUID = createMember()
        val commentId = createComment()
        val content1 = "test_reply1"
        val request1 = CreateReply(memberUUID, commentId, content1)
        replyCommandService.createReply(request1)
        flushAndClear()
        val content2 = "test_reply2"
        val request2 = CreateReply(memberUUID, commentId, content2)
        replyCommandService.createReply(request2)
        flushAndClear()

        //when
        val replies = replyQueryService.getRepliesByWriter(memberUUID, null)

        //then
        Assertions.assertThat(replies).isNotEmpty
    }

    @Test @Transactional
    fun getRepliesByWriterPagingTest() {
        //given
        val memberUUID = createMember()
        val commentId = createComment()
        val content1 = "test_reply1"
        val request1 = CreateReply(memberUUID, commentId, content1)
        replyCommandService.createReply(request1)
        flushAndClear()
        val content2 = "test_reply2"
        val request2 = CreateReply(memberUUID, commentId, content2)
        val replyId2 = replyCommandService.createReply(request2)
        flushAndClear()

        //when
        val replies = replyQueryService.getRepliesByWriter(memberUUID, replyId2)

        //then
        Assertions.assertThat(replies).isNotEmpty
    }

    @Test @Transactional
    fun getRepliesByCommentTest() {
        //given
        val memberUUID = createMember()
        val commentId = createComment()
        val content1 = "test_reply1"
        val request1 = CreateReply(memberUUID, commentId, content1)
        replyCommandService.createReply(request1)
        flushAndClear()
        val content2 = "test_reply2"
        val request2 = CreateReply(memberUUID, commentId, content2)
        replyCommandService.createReply(request2)
        flushAndClear()

        //when
        val replies = replyQueryService.getRepliesByComment(commentId, null)

        //then
        Assertions.assertThat(replies).isNotEmpty
    }

    @Test @Transactional
    fun getRepliesByCommentPagingTest() {
        //given
        val memberUUID = createMember()
        val commentId = createComment()
        val content1 = "test_reply1"
        val request1 = CreateReply(memberUUID, commentId, content1)
        replyCommandService.createReply(request1)
        flushAndClear()
        val content2 = "test_reply2"
        val request2 = CreateReply(memberUUID, commentId, content2)
        val replyId2 = replyCommandService.createReply(request2)
        flushAndClear()

        //when
        val replies = replyQueryService.getRepliesByComment(commentId, replyId2)

        //then
        Assertions.assertThat(replies).isNotEmpty
    }
}