package howru.howru.post.service.command

import howru.howru.exception.exception.PostException
import howru.howru.logger
import howru.howru.member.dto.request.SignupRequest
import howru.howru.member.service.command.MemberCommandService
import howru.howru.post.dto.request.CreatePost
import howru.howru.post.dto.request.DeletePost
import howru.howru.post.dto.update.UpdateContent
import howru.howru.post.service.query.PostQueryService
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.util.*

@SpringBootTest
class PostCommandServiceTest @Autowired constructor(
    private val entityManager: EntityManager,
    private val memberCommandService: MemberCommandService,
    private val postCommandService: PostCommandService,
    private val postQueryService: PostQueryService
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

    @Test
    @Transactional
    fun createPostTest() {
        //given
        val writerUUID = createWriter()
        val content = "test_content"

        //when
        val request = CreatePost(writerUUID, content)
        val postUUID = postCommandService.createPost(request)
        flushAndClear()

        //then
        val post = postQueryService.getPostByUUID(postUUID)
        logger().info("${post.createdDate}")
        Assertions.assertThat(post.writerUUID)
            .isEqualTo(writerUUID)
    }

    @Test
    @Transactional
    fun editContentTest() {
        //given
        val writerUUID = createWriter()
        val content = "test_content"
        val request = CreatePost(writerUUID, content)
        val postUUID = postCommandService.createPost(request)
        flushAndClear()

        //when
        val updatedContent = "updated_content"
        val updateRequest = UpdateContent(postUUID, writerUUID, updatedContent)
        postCommandService.editContent(updateRequest)
        flushAndClear()

        //then
        Assertions.assertThat(postQueryService.getPostByUUID(postUUID).content)
            .isEqualTo(updatedContent)
    }

    @Test
    @Transactional
    fun deletePostTest() {
        //given
        val writerUUID = createWriter()
        val content = "test_content"
        val request = CreatePost(writerUUID, content)
        val postUUID = postCommandService.createPost(request)
        flushAndClear()

        //when
        val deleteRequest = DeletePost(postUUID, writerUUID)
        postCommandService.deletePost(deleteRequest)
        flushAndClear()

        //then
        Assertions.assertThatThrownBy { postQueryService.getPostByUUID(postUUID) }
            .isInstanceOf(PostException::class.java)
    }
}