package howru.howru.post.service.command

import howru.howru.post.exception.PostException
import howru.howru.logger
import howru.howru.member.dto.request.LoginRequest
import howru.howru.member.dto.request.SignupRequest
import howru.howru.member.service.command.MemberCommandService
import howru.howru.post.domain.PostState
import howru.howru.post.dto.request.CreatePost
import howru.howru.post.dto.request.RemovePost
import howru.howru.post.dto.request.UpdatePostContent
import howru.howru.post.service.query.PostQueryService
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.util.*

@SpringBootTest
class PostCommandServiceTest
    @Autowired
    constructor(
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
            val nickName = "writer"
            val request = SignupRequest(email, pw, nickName)
            memberCommandService.signup(request)
            flushAndClear()
            val loginRequest = LoginRequest(email, pw)
            return memberCommandService.login(loginRequest).id
        }

        @Test
        @Transactional
        fun createPostTest() {
            // given
            val writerId = createWriter()
            val content = "test_content"

            // when
            val request = CreatePost(writerId, content)
            val postId = postCommandService.createPost(request)
            flushAndClear()

            // then
            val post = postQueryService.getPostById(postId)
            logger().info("${post.createdDatetime}")
            Assertions.assertThat(post.writerId)
                .isEqualTo(writerId)
        }

        @Test
        @Transactional
        fun editContentTest() {
            // given
            val writerId = createWriter()
            val content = "test_content"
            val request = CreatePost(writerId, content)
            val postId = postCommandService.createPost(request)
            flushAndClear()

            // when
            val updatedContent = "updated_content"
            val updateRequest = UpdatePostContent(writerId, updatedContent)
            postCommandService.editPostContent(postId, updateRequest)
            flushAndClear()

            // then
            val post = postQueryService.getPostById(postId)
            Assertions.assertThat(post.content).isEqualTo(updatedContent)
            Assertions.assertThat(post.postState).isEqualTo(PostState.EDITED)
        }

        @Test
        @Transactional
        fun removePostTest() {
            // given
            val writerId = createWriter()
            val content = "test_content"
            val request = CreatePost(writerId, content)
            val postId = postCommandService.createPost(request)
            flushAndClear()

            // when
            val deleteRequest = RemovePost(writerId)
            postCommandService.removePost(postId, deleteRequest)
            flushAndClear()

            // then
            Assertions.assertThatThrownBy { postQueryService.getPostById(postId) }
                .isInstanceOf(PostException::class.java)
        }
    }
