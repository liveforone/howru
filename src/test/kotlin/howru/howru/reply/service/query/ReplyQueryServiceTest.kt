package howru.howru.reply.service.query

import howru.howru.comments.dto.CreateComments
import howru.howru.comments.service.command.CommentsCommandService
import howru.howru.member.dto.LoginRequest
import howru.howru.member.dto.SignupRequest
import howru.howru.member.service.command.MemberCommandService
import howru.howru.post.dto.CreatePost
import howru.howru.post.service.command.PostCommandService
import howru.howru.reply.dto.CreateReply
import howru.howru.reply.service.command.ReplyCommandService
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.util.*

@SpringBootTest
class ReplyQueryServiceTest
    @Autowired
    constructor(
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
            memberCommandService.signup(request)
            flushAndClear()
            val loginRequest = LoginRequest(email, pw)
            return memberCommandService.login(loginRequest).id
        }

        private fun createCommentWriter(): UUID {
            val email = "test_comment_writer@gmail.com"
            val pw = "1122"
            val nickName = "cWriter"
            val request = SignupRequest(email, pw, nickName)
            memberCommandService.signup(request)
            flushAndClear()
            val loginRequest = LoginRequest(email, pw)
            return memberCommandService.login(loginRequest).id
        }

        private fun createMember(): UUID {
            val email = "test_member@gmail.com"
            val pw = "3344"
            val nickName = "member"
            val request = SignupRequest(email, pw, nickName)
            memberCommandService.signup(request)
            flushAndClear()
            val loginRequest = LoginRequest(email, pw)
            return memberCommandService.login(loginRequest).id
        }

        private fun createPost(): Long {
            val writerId = createPostWriter()
            val content = "test_content"
            val request = CreatePost(writerId, content)
            val postId = postCommandService.createPost(request)
            flushAndClear()
            return postId
        }

        private fun createComment(): Long {
            val memberId = createCommentWriter()
            val postId = createPost()
            val content = "test_comment"
            val request = CreateComments(memberId, postId, content)
            val commentId = commentsCommandService.createComments(request)
            flushAndClear()
            return commentId
        }

        @Test @Transactional
        fun getReplyByIdTest() {
            // given
            val memberId = createMember()
            val commentId = createComment()
            val content = "test_reply"
            val request = CreateReply(memberId, commentId, content)
            val replyId = replyCommandService.createReply(request)
            flushAndClear()

            // when
            val reply = replyQueryService.getReplyById(replyId)

            // then
            Assertions.assertThat(reply).isNotNull
        }

        @Test @Transactional
        fun getRepliesByWriterTest() {
            // given
            val memberId = createMember()
            val commentId = createComment()
            val content1 = "test_reply1"
            val request1 = CreateReply(memberId, commentId, content1)
            replyCommandService.createReply(request1)
            flushAndClear()
            val content2 = "test_reply2"
            val request2 = CreateReply(memberId, commentId, content2)
            replyCommandService.createReply(request2)
            flushAndClear()

            // when
            val replyPage = replyQueryService.getRepliesByWriter(memberId, null)

            // then
            Assertions.assertThat(replyPage.replyInfoList).isNotEmpty
        }

        @Test @Transactional
        fun getRepliesByWriterPagingTest() {
            // given
            val memberId = createMember()
            val commentId = createComment()
            val content1 = "test_reply1"
            val request1 = CreateReply(memberId, commentId, content1)
            replyCommandService.createReply(request1)
            flushAndClear()
            val content2 = "test_reply2"
            val request2 = CreateReply(memberId, commentId, content2)
            replyCommandService.createReply(request2)
            flushAndClear()

            // when
            val replyPage = replyQueryService.getRepliesByWriter(memberId, null)

            // then
            Assertions.assertThat(replyPage.replyInfoList).isNotEmpty
        }

        @Test @Transactional
        fun getRepliesByCommentTest() {
            // given
            val memberId = createMember()
            val commentId = createComment()
            val content1 = "test_reply1"
            val request1 = CreateReply(memberId, commentId, content1)
            replyCommandService.createReply(request1)
            flushAndClear()
            val content2 = "test_reply2"
            val request2 = CreateReply(memberId, commentId, content2)
            replyCommandService.createReply(request2)
            flushAndClear()

            // when
            val replyPage = replyQueryService.getRepliesByComment(commentId, null)

            // then
            Assertions.assertThat(replyPage.replyInfoList).isNotEmpty
        }

        @Test @Transactional
        fun getRepliesByCommentPagingTest() {
            // given
            val memberId = createMember()
            val commentId = createComment()
            val content1 = "test_reply1"
            val request1 = CreateReply(memberId, commentId, content1)
            replyCommandService.createReply(request1)
            flushAndClear()
            val content2 = "test_reply2"
            val request2 = CreateReply(memberId, commentId, content2)
            replyCommandService.createReply(request2)
            flushAndClear()

            // when
            val replyPage = replyQueryService.getRepliesByComment(commentId, null)

            // then
            Assertions.assertThat(replyPage.replyInfoList).isNotEmpty
        }
    }
