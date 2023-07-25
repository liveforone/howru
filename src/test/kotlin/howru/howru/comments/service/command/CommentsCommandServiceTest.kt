package howru.howru.comments.service.command

import howru.howru.comments.domain.CommentsState
import howru.howru.comments.dto.request.CreateComments
import howru.howru.comments.dto.request.DeleteComments
import howru.howru.comments.dto.update.UpdateCommentsContent
import howru.howru.comments.service.query.CommentsQueryService
import howru.howru.exception.exception.CommentsException
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
class CommentsCommandServiceTest @Autowired constructor(
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

    @Test @Transactional
    fun createCommentTest() {
        //given
        val memberUUID = createMember()
        val postUUID = createPost()
        val content = "test_comments"

        //when
        val request = CreateComments(memberUUID, postUUID, content)
        val commentUUID = commentsCommandService.createComment(request)
        flushAndClear()

        //then
        Assertions.assertThat(commentsQueryService.getCommentByUUID(commentUUID).content)
            .isEqualTo(content)
    }

    @Test @Transactional
    fun editContentTest() {
        //given
        val memberUUID = createMember()
        val postUUID = createPost()
        val request = CreateComments(memberUUID, postUUID, "test_comments")
        val commentUUID = commentsCommandService.createComment(request)
        flushAndClear()

        //when
        val updateContent = "updated_comment"
        val updateRequest = UpdateCommentsContent(commentUUID, memberUUID, updateContent)
        commentsCommandService.editContent(updateRequest)
        flushAndClear()

        //then
        val comment = commentsQueryService.getCommentByUUID(commentUUID)
        Assertions.assertThat(comment.content).isEqualTo(updateContent)
        Assertions.assertThat(comment.commentsState).isEqualTo(CommentsState.EDITED)
    }

    @Test @Transactional
    fun deleteCommentTest() {
        //given
        val memberUUID = createMember()
        val postUUID = createPost()
        val request = CreateComments(memberUUID, postUUID, "test_comments")
        val commentUUID = commentsCommandService.createComment(request)
        flushAndClear()

        //when
        val deleteRequest = DeleteComments(commentUUID, memberUUID)
        commentsCommandService.deleteComment(deleteRequest)
        flushAndClear()

        //then
        Assertions.assertThatThrownBy { commentsQueryService.getCommentByUUID(commentUUID) }
            .isInstanceOf(CommentsException::class.java)
    }
}