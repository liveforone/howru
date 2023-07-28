package howru.howru.reply.service.query

import howru.howru.comments.dto.request.CreateComments
import howru.howru.comments.service.command.CommentsCommandService
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
    fun getReplyByUUIDTest() {
        //given
        val memberUUID = createMember()
        val commentUUID = createComment()
        val content = "test_reply"
        val request = CreateReply(memberUUID, commentUUID, content)
        val replyUUID = replyCommandService.createReply(request)
        flushAndClear()

        //when
        val reply = replyQueryService.getReplyByUUID(replyUUID)

        //then
        Assertions.assertThat(reply).isNotNull
    }

    @Test @Transactional
    fun getRepliesByWriterTest() {
        //given
        val memberUUID = createMember()
        val commentUUID = createComment()
        val content1 = "test_reply1"
        val request1 = CreateReply(memberUUID, commentUUID, content1)
        replyCommandService.createReply(request1)
        flushAndClear()
        val content2 = "test_reply2"
        val request2 = CreateReply(memberUUID, commentUUID, content2)
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
        val commentUUID = createComment()
        val content1 = "test_reply1"
        val request1 = CreateReply(memberUUID, commentUUID, content1)
        replyCommandService.createReply(request1)
        flushAndClear()
        val content2 = "test_reply2"
        val request2 = CreateReply(memberUUID, commentUUID, content2)
        val replyUUID2 = replyCommandService.createReply(request2)
        flushAndClear()

        //when
        val replies = replyQueryService.getRepliesByWriter(memberUUID, replyUUID2)

        //then
        Assertions.assertThat(replies).isNotEmpty
    }

    @Test @Transactional
    fun getRepliesByCommentTest() {
        //given
        val memberUUID = createMember()
        val commentUUID = createComment()
        val content1 = "test_reply1"
        val request1 = CreateReply(memberUUID, commentUUID, content1)
        replyCommandService.createReply(request1)
        flushAndClear()
        val content2 = "test_reply2"
        val request2 = CreateReply(memberUUID, commentUUID, content2)
        replyCommandService.createReply(request2)
        flushAndClear()

        //when
        val replies = replyQueryService.getRepliesByComment(commentUUID, null)

        //then
        Assertions.assertThat(replies).isNotEmpty
    }

    @Test @Transactional
    fun getRepliesByCommentPagingTest() {
        //given
        val memberUUID = createMember()
        val commentUUID = createComment()
        val content1 = "test_reply1"
        val request1 = CreateReply(memberUUID, commentUUID, content1)
        replyCommandService.createReply(request1)
        flushAndClear()
        val content2 = "test_reply2"
        val request2 = CreateReply(memberUUID, commentUUID, content2)
        val replyUUID2 = replyCommandService.createReply(request2)
        flushAndClear()

        //when
        val replies = replyQueryService.getRepliesByComment(commentUUID, replyUUID2)

        //then
        Assertions.assertThat(replies).isNotEmpty
    }
}