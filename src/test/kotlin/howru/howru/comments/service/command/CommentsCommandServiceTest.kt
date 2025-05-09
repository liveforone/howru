package howru.howru.comments.service.command

import howru.howru.comments.domain.CommentsState
import howru.howru.comments.dto.request.CreateComments
import howru.howru.comments.dto.request.RemoveComments
import howru.howru.comments.dto.request.UpdateComments
import howru.howru.comments.service.query.CommentsQueryService
import howru.howru.comments.exception.CommentsException
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
class CommentsCommandServiceTest
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
            val nickName = "member"
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

        @Test @Transactional
        fun createCommentTest() {
            // given
            val memberId = createMember()
            val postId = createPost()
            val content = "test_comments"

            // when
            val request = CreateComments(memberId, postId, content)
            val commentId = commentsCommandService.createComments(request)
            flushAndClear()

            // then
            Assertions
                .assertThat(commentsQueryService.getCommentById(commentId).content)
                .isEqualTo(content)
        }

        @Test @Transactional
        fun editCommentTest() {
            // given
            val memberId = createMember()
            val postId = createPost()
            val request = CreateComments(memberId, postId, "test_comments")
            val commentId = commentsCommandService.createComments(request)
            flushAndClear()

            // when
            val updateContent = "updated_comment"
            val updateRequest = UpdateComments(memberId, updateContent)
            commentsCommandService.editComment(commentId, updateRequest)
            flushAndClear()

            // then
            val comment = commentsQueryService.getCommentById(commentId)
            Assertions.assertThat(comment.content).isEqualTo(updateContent)
            Assertions.assertThat(comment.commentsState).isEqualTo(CommentsState.EDITED)
        }

        @Test @Transactional
        fun removeCommentTest() {
            // given
            val memberId = createMember()
            val postId = createPost()
            val request = CreateComments(memberId, postId, "test_comments")
            val commentId = commentsCommandService.createComments(request)
            flushAndClear()

            // when
            val deleteRequest = RemoveComments(memberId)
            commentsCommandService.removeComment(commentId, deleteRequest)
            flushAndClear()

            // then
            Assertions
                .assertThatThrownBy { commentsQueryService.getCommentById(commentId) }
                .isInstanceOf(CommentsException::class.java)
        }
    }
