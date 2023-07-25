package howru.howru.comments.service.query

import howru.howru.comments.domain.CommentsState
import howru.howru.comments.dto.request.CreateComments
import howru.howru.comments.service.command.CommentsCommandService
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
class CommentsQueryServiceTest @Autowired constructor(
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
        val writerUUID = createWriter()
        val content = "test_content"
        val request = CreatePost(writerUUID, content)
        val postUUID = postCommandService.createPost(request)
        flushAndClear()
        return postUUID
    }

    @Test
    @Transactional
    fun getCommentByUUIDTest() {
        //given
        val memberUUID = createMember()
        val postUUID = createPost()
        val content = "test_comments"
        val request = CreateComments(memberUUID, postUUID, content)
        val commentUUID = commentsCommandService.createComment(request)
        flushAndClear()

        //when
        val comment = commentsQueryService.getCommentByUUID(commentUUID)

        //then
        Assertions.assertThat(comment.commentsState).isEqualTo(CommentsState.ORIGINAL)
    }

    @Test
    @Transactional
    fun getCommentsByWriterTest() {
        //given
        val memberUUID = createMember()
        val postUUID = createPost()
        val content = "test_comments"
        repeat(2) {
            val request = CreateComments(memberUUID, postUUID, content)
            commentsCommandService.createComment(request)
            flushAndClear()
        }

        //when
        val comments = commentsQueryService.getCommentsByWriter(memberUUID, null)

        //then
        Assertions.assertThat(comments.size).isEqualTo(2)
    }

    @Test
    @Transactional
    fun getCommentsByWriterPagingTest() {
        //given
        val memberUUID = createMember()
        val postUUID = createPost()
        val content = "test_comments"
        val request1 = CreateComments(memberUUID, postUUID, content)
        commentsCommandService.createComment(request1)
        flushAndClear()
        val request2 = CreateComments(memberUUID, postUUID, content)
        val commentUUID2 = commentsCommandService.createComment(request2)
        flushAndClear()

        //when
        val comments = commentsQueryService.getCommentsByWriter(memberUUID, commentUUID2)

        //then
        Assertions.assertThat(comments.size).isEqualTo(1)
    }

    @Test
    @Transactional
    fun getCommentsByPostTest() {
        //given
        val memberUUID = createMember()
        val postUUID = createPost()
        val content = "test_comments"
        repeat(2) {
            val request = CreateComments(memberUUID, postUUID, content)
            commentsCommandService.createComment(request)
            flushAndClear()
        }

        //when
        val comments = commentsQueryService.getCommentsByPost(postUUID, null)

        //then
        Assertions.assertThat(comments.size).isEqualTo(2)
    }

    @Test
    @Transactional
    fun getCommentsByPostPagingTest() {
        //given
        val memberUUID = createMember()
        val postUUID = createPost()
        val content = "test_comments"
        val request1 = CreateComments(memberUUID, postUUID, content)
        commentsCommandService.createComment(request1)
        flushAndClear()
        val request2 = CreateComments(memberUUID, postUUID, content)
        val commentUUID2 = commentsCommandService.createComment(request2)
        flushAndClear()

        //when
        val comments = commentsQueryService.getCommentsByPost(postUUID, commentUUID2)

        //then
        Assertions.assertThat(comments.size).isEqualTo(1)
    }
}