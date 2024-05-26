package howru.howru.comments.service.query

import howru.howru.comments.domain.CommentsState
import howru.howru.comments.dto.request.CreateComments
import howru.howru.comments.service.command.CommentsCommandService
import howru.howru.member.dto.request.LoginRequest
import howru.howru.member.dto.request.SignupRequest
import howru.howru.member.service.command.MemberCommandService
import howru.howru.post.dto.request.CreatePost
import howru.howru.post.service.command.PostCommandService
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.util.*

@SpringBootTest
class CommentsQueryServiceTest
    @Autowired
    constructor(
        private val entityManager: EntityManager,
        private val memberCommandService: MemberCommandService,
        private val postCommandService: PostCommandService,
        private val commentsCommandService: CommentsCommandService,
        private val commentsQueryService: CommentsQueryService
    ) {
        private fun flushAndClear() {
            entityManager.flush()
            entityManager.clear()
        }

        private fun createWriter(): UUID {
            val email = "test_writer@gmail.com"
            val pw = "1122"
            val nickName = "writer"
            val request = SignupRequest(email, pw, nickName)
            memberCommandService.signup(request)
            flushAndClear()
            val loginRequest = LoginRequest(email, pw)
            return memberCommandService.login(loginRequest).id
        }

        private fun createMember(): UUID {
            val email = "test_member@gmail.com"
            val pw = "3344"
            val nickName = "member1"
            val request = SignupRequest(email, pw, nickName)
            memberCommandService.signup(request)
            flushAndClear()
            val loginRequest = LoginRequest(email, pw)
            return memberCommandService.login(loginRequest).id
        }

        private fun createPost(): Long {
            val writerId = createWriter()
            val content = "test_content"
            val request = CreatePost(writerId, content)
            val postId = postCommandService.createPost(request)
            flushAndClear()
            return postId
        }

        @Test
        @Transactional
        fun getCommentByIdTest() {
            // given
            val memberId = createMember()
            val postId = createPost()
            val content = "test_comments"
            val request = CreateComments(memberId, postId, content)
            val commentId = commentsCommandService.createComments(request)
            flushAndClear()

            // when
            val comment = commentsQueryService.getCommentById(commentId)

            // then
            Assertions.assertThat(comment.commentsState).isEqualTo(CommentsState.ORIGINAL)
        }

        @Test
        @Transactional
        fun getCommentsByMemberTest() {
            // given
            val memberId = createMember()
            val postId = createPost()
            val content = "test_comments"
            repeat(2) {
                val request = CreateComments(memberId, postId, content)
                commentsCommandService.createComments(request)
                flushAndClear()
            }

            // when
            val commentsPage = commentsQueryService.getCommentsByMember(memberId, null)

            // then
            Assertions.assertThat(commentsPage.commentsInfoList.size).isEqualTo(2)
        }

        @Test
        @Transactional
        fun getCommentsByMemberPagingTest() {
            // given
            val memberId = createMember()
            val postId = createPost()
            val content = "test_comments"
            val request1 = CreateComments(memberId, postId, content)
            commentsCommandService.createComments(request1)
            flushAndClear()
            val request2 = CreateComments(memberId, postId, content)
            commentsCommandService.createComments(request2)
            flushAndClear()

            // when
            val commentsPage = commentsQueryService.getCommentsByMember(memberId, null)

            // then
            Assertions.assertThat(commentsPage.commentsInfoList.size).isEqualTo(2)
        }

        @Test
        @Transactional
        fun getCommentsByPostTest() {
            // given
            val memberId = createMember()
            val postId = createPost()
            val content = "test_comments"
            repeat(2) {
                val request = CreateComments(memberId, postId, content)
                commentsCommandService.createComments(request)
                flushAndClear()
            }

            // when
            val commentsPage = commentsQueryService.getCommentsByPost(postId, null)

            // then
            Assertions.assertThat(commentsPage.commentsInfoList.size).isEqualTo(2)
        }

        @Test
        @Transactional
        fun getCommentsByPostPagingTest() {
            // given
            val memberId = createMember()
            val postId = createPost()
            val content = "test_comments"
            val request1 = CreateComments(memberId, postId, content)
            commentsCommandService.createComments(request1)
            flushAndClear()
            val request2 = CreateComments(memberId, postId, content)
            commentsCommandService.createComments(request2)
            flushAndClear()

            // when
            val commentsPage = commentsQueryService.getCommentsByPost(postId, null)

            // then
            Assertions.assertThat(commentsPage.commentsInfoList.size).isEqualTo(2)
        }
    }
