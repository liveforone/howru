package howru.howru.likes.service.command

import howru.howru.likes.dto.request.CreateLikes
import howru.howru.likes.dto.request.RemoveLikes
import howru.howru.likes.service.query.LikesQueryService
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
class LikesCommandServiceTest @Autowired constructor(
    private val entityManager: EntityManager,
    private val memberCommandService: MemberCommandService,
    private val postCommandService: PostCommandService,
    private val likesCommandService: LikesCommandService,
    private val likesQueryService: LikesQueryService
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
        memberCommandService.signupMember(request)
        flushAndClear()
        val loginRequest = LoginRequest(email, pw)
        return memberCommandService.login(loginRequest).id
    }

    private fun createMember(): UUID {
        val email = "test_member@gmail.com"
        val pw = "3344"
        val nickName = "member"
        val request = SignupRequest(email, pw, nickName)
        memberCommandService.signupMember(request)
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
    fun createLikesTest() {
        //given
        val memberId = createMember()
        val postId = createPost()

        //when
        val request = CreateLikes(memberId, postId)
        likesCommandService.createLikes(request)
        flushAndClear()

        //then
        Assertions.assertThat(likesQueryService.getLikesBelongPost(postId, null))
            .isNotEmpty
    }

    @Test
    @Transactional
    fun deleteLikesTest() {
        //given
        val memberId = createMember()
        val postId = createPost()
        val request = CreateLikes(memberId, postId)
        likesCommandService.createLikes(request)
        flushAndClear()

        //when
        val deleteRequest = RemoveLikes(memberId, postId)
        likesCommandService.removeLikes(deleteRequest)
        flushAndClear()

        //then
        Assertions.assertThat(likesQueryService.getLikesBelongMember(memberId, null))
            .isEmpty()
    }
}