package howru.howru.comments.service.query

import howru.howru.comments.domain.CommentsState
import howru.howru.comments.dto.request.CreateComments
import howru.howru.comments.service.command.CommentsCommandService
import howru.howru.member.dto.request.LoginRequest
import howru.howru.member.dto.request.SignupRequest
import howru.howru.member.service.command.MemberCommandService
import howru.howru.post.dto.request.CreatePost
import howru.howru.post.service.command.PostCommandService
import howru.howru.subscribe.dto.request.CreateSubscribe
import howru.howru.subscribe.service.command.SubscribeCommandService
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
    private val subscribeCommandService: SubscribeCommandService,
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
        memberCommandService.signupMember(request)
        flushAndClear()
        val loginRequest = LoginRequest(email, pw)
        return memberCommandService.login(loginRequest).uuid
    }

    private fun createMember(): UUID {
        val email = "test_member@gmail.com"
        val pw = "3344"
        val nickName = "member1"
        val request = SignupRequest(email, pw, nickName)
        memberCommandService.signupMember(request)
        flushAndClear()
        val loginRequest = LoginRequest(email, pw)
        return memberCommandService.login(loginRequest).uuid
    }

    private fun createMember2ForSubscribe(): UUID {
        val email = "test_member2@gmail.com"
        val pw = "5566"
        val nickName = "member2"
        val request = SignupRequest(email, pw, nickName)
        memberCommandService.signupMember(request)
        flushAndClear()
        val loginRequest = LoginRequest(email, pw)
        return memberCommandService.login(loginRequest).uuid
    }

    private fun createPost(): Long {
        val writerUUID = createWriter()
        val content = "test_content"
        val request = CreatePost(writerUUID, content)
        val postId = postCommandService.createPost(request)
        flushAndClear()
        return postId
    }

    private fun createSubscribe(followeeUUID: UUID, followerUUID: UUID) {
        subscribeCommandService.createSubscribe(CreateSubscribe(followeeUUID, followerUUID))
        flushAndClear()
    }

    @Test
    @Transactional
    fun getCommentByIdTest() {
        //given
        val memberUUID = createMember()
        val postId = createPost()
        val content = "test_comments"
        val request = CreateComments(memberUUID, postId, content)
        val commentId = commentsCommandService.createComments(request)
        flushAndClear()

        //when
        val comment = commentsQueryService.getCommentById(commentId)

        //then
        Assertions.assertThat(comment.commentsState).isEqualTo(CommentsState.ORIGINAL)
    }

    @Test
    @Transactional
    fun getCommentsByWriterTest() {
        //given
        val memberUUID = createMember()
        val postId = createPost()
        val content = "test_comments"
        repeat(2) {
            val request = CreateComments(memberUUID, postId, content)
            commentsCommandService.createComments(request)
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
        val postId = createPost()
        val content = "test_comments"
        val request1 = CreateComments(memberUUID, postId, content)
        commentsCommandService.createComments(request1)
        flushAndClear()
        val request2 = CreateComments(memberUUID, postId, content)
        val commentId2 = commentsCommandService.createComments(request2)
        flushAndClear()

        //when
        val comments = commentsQueryService.getCommentsByWriter(memberUUID, commentId2)

        //then
        Assertions.assertThat(comments.size).isEqualTo(1)
    }

    @Test
    @Transactional
    fun getCommentsByPostTest() {
        //given
        val memberUUID = createMember()
        val postId = createPost()
        val content = "test_comments"
        repeat(2) {
            val request = CreateComments(memberUUID, postId, content)
            commentsCommandService.createComments(request)
            flushAndClear()
        }

        //when
        val comments = commentsQueryService.getCommentsByPost(postId, null)

        //then
        Assertions.assertThat(comments.size).isEqualTo(2)
    }

    @Test
    @Transactional
    fun getCommentsByPostPagingTest() {
        //given
        val memberUUID = createMember()
        val postId = createPost()
        val content = "test_comments"
        val request1 = CreateComments(memberUUID, postId, content)
        commentsCommandService.createComments(request1)
        flushAndClear()
        val request2 = CreateComments(memberUUID, postId, content)
        val commentId2 = commentsCommandService.createComments(request2)
        flushAndClear()

        //when
        val comments = commentsQueryService.getCommentsByPost(postId, commentId2)

        //then
        Assertions.assertThat(comments.size).isEqualTo(1)
    }

    @Test
    @Transactional
    fun getCommentsBySomeoneTest() {
        //given
        val memberUUID = createMember()
        val member2UUID = createMember2ForSubscribe()
        createSubscribe(memberUUID, member2UUID)
        createSubscribe(member2UUID, memberUUID)
        val postId = createPost()
        val content = "test_comments"
        val request1 = CreateComments(memberUUID, postId, content)
        commentsCommandService.createComments(request1)
        flushAndClear()

        //when
        val comments = commentsQueryService.getCommentsBySomeone(memberUUID, member2UUID, null)

        //then
        Assertions.assertThat(comments).isNotEmpty
    }

    @Test
    @Transactional
    fun getCommentsBySomeonePagingTest() {
        //given
        val memberUUID = createMember()
        val member2UUID = createMember2ForSubscribe()
        createSubscribe(memberUUID, member2UUID)
        createSubscribe(member2UUID, memberUUID)
        val postId = createPost()
        val content = "test_comments"
        val request1 = CreateComments(memberUUID, postId, content)
        val commentId = commentsCommandService.createComments(request1)
        flushAndClear()

        //when
        val comments = commentsQueryService.getCommentsBySomeone(memberUUID, member2UUID, commentId)

        //then
        Assertions.assertThat(comments).isEmpty()
    }
}