package howru.howru.likes.service.query

import howru.howru.likes.dto.request.CreateLikes
import howru.howru.likes.service.command.LikesCommandService
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
class LikesQueryServiceTest @Autowired constructor(
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
    fun getLikesBelongMemberPagingTest() {
        //given
        val memberUUID = createMember()
        val postUUID = createPost()
        val request = CreateLikes(memberUUID, postUUID)
        likesCommandService.createLikes(request)
        flushAndClear()

        //when
        val likes = likesQueryService.getLikesBelongMember(memberUUID, postUUID)

        //then
        Assertions.assertThat(likes).isEmpty()
    }

    @Test @Transactional
    fun getLikesBelongPostPagingTest() {
        //given
        val memberUUID = createMember()
        val postUUID = createPost()
        val request = CreateLikes(memberUUID, postUUID)
        likesCommandService.createLikes(request)
        flushAndClear()

        //when
        val likes = likesQueryService.getLikesBelongPost(postUUID, memberUUID)

        //then
        Assertions.assertThat(likes).isEmpty()
    }
}