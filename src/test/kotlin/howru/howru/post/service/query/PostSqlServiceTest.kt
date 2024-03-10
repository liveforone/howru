package howru.howru.post.service.query

import howru.howru.exception.exception.SubscribeException
import howru.howru.logger
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
class PostSqlServiceTest @Autowired constructor(
    private val entityManager: EntityManager,
    private val memberCommandService: MemberCommandService,
    private val subscribeCommandService: SubscribeCommandService,
    private val postCommandService: PostCommandService,
    private val postQueryService: PostQueryService
) {

    private fun flushAndClear() {
        entityManager.flush()
        entityManager.clear()
    }

    private fun createWriter1(): UUID {
        val email = "test_followee@gmail.com"
        val pw = "1111"
        val nickName = "writer1"
        val request = SignupRequest(email, pw, nickName)
        memberCommandService.signup(request)
        flushAndClear()
        val loginRequest = LoginRequest(email, pw)
        return memberCommandService.login(loginRequest).id
    }

    private fun createWriter2(): UUID {
        val email = "test_follower@gmail.com"
        val pw = "2222"
        val nickName = "writer2"
        val request = SignupRequest(email, pw, nickName)
        memberCommandService.signup(request)
        flushAndClear()
        val loginRequest = LoginRequest(email, pw)
        return memberCommandService.login(loginRequest).id
    }

    @Test
    @Transactional
    fun getPostByIdTest() {
        //given
        val writerId = createWriter1()
        val content = "test_content"
        val request = CreatePost(writerId, content)
        val postId = postCommandService.createPost(request)
        flushAndClear()

        //when
        val post = postQueryService.getPostById(postId)

        //then
        Assertions.assertThat(post.content).isEqualTo(content)
    }

    @Test
    @Transactional
    fun getMyPostsTest() {
        //given
        val writerId = createWriter1()
        val content1 = "test_content1"
        val request1 = CreatePost(writerId, content1)
        postCommandService.createPost(request1)
        flushAndClear()
        val content2 = "test_content2"
        val request2 = CreatePost(writerId, content2)
        postCommandService.createPost(request2)
        flushAndClear()

        //when
        val myPosts = postQueryService.getMyPosts(writerId, 0)

        //then
        Assertions.assertThat(myPosts.size).isEqualTo(2)
    }

    @Test
    @Transactional
    fun getAllPostsTest() {
        /*
        * 최신순 정렬 테스트
         */
        //given
        val writerId1 = createWriter1()
        val content1 = "test_content1"
        val request1 = CreatePost(writerId1, content1)
        postCommandService.createPost(request1)
        flushAndClear()
        val writerId2 = createWriter2()
        val content2 = "test_content2"
        val request2 = CreatePost(writerId2, content2)
        postCommandService.createPost(request2)
        flushAndClear()

        //when
        val allPosts = postQueryService.getAllPosts(0)

        //then
        Assertions.assertThat(allPosts[0]?.content ?: "").isEqualTo(content2)
    }

    /*
    * 구독하지 않았을때, 상대방의 게시글을 조회하는 경우 예외가 정상적으로 발생하는지 확인하는 테스트
    * PostException 이 발생해야한다.
     */
    @Test
    @Transactional
    fun getPostsBySomeoneWhenNoSubscribeTest() {
        //given
        val followeeId = createWriter1()
        memberCommandService.memberLockOn(followeeId)
        flushAndClear()
        val content1 = "test_content1"
        val request1 = CreatePost(followeeId, content1)
        postCommandService.createPost(request1)
        flushAndClear()
        val followerId = createWriter2()

        //then -> error 발생!!
        Assertions.assertThatThrownBy { postQueryService.getPostsBySomeone(followeeId, followerId, 0) }
            .isInstanceOf(SubscribeException::class.java)
    }

    /*
    * 구독 한 경우, 상대방의 게시글을 조회할때 정상적으로 처리되는지 확인하는 테스트
    * 구독확인에 대한 쿼리까지 살펴볼 수 있다.
     */
    @Test
    @Transactional
    fun getPostsBySomeoneWhenSubscribeTest() {
        //given
        val followeeId = createWriter1()
        memberCommandService.memberLockOn(followeeId)
        flushAndClear()
        val content1 = "test_content1"
        val request1 = CreatePost(followeeId, content1)
        postCommandService.createPost(request1)
        flushAndClear()
        val followerId = createWriter2()

        //when
        val subscribeRequest = CreateSubscribe(followeeId, followerId)
        subscribeCommandService.createSubscribe(subscribeRequest)
        flushAndClear()

        //then
        Assertions.assertThat(postQueryService.getPostsBySomeone(followeeId, followerId, 0))
            .isNotEmpty
    }

    @Test
    @Transactional
    fun getPostsOfFolloweeTest() {
        //given
        val followeeId = createWriter1()
        val content1 = "test_content1"
        val request1 = CreatePost(followeeId, content1)
        postCommandService.createPost(request1)
        flushAndClear()
        val followerId = createWriter2()

        //when
        val subscribeRequest = CreateSubscribe(followeeId, followerId)
        subscribeCommandService.createSubscribe(subscribeRequest)
        flushAndClear()

        //then
        Assertions.assertThat(postQueryService.getPostsOfFollowee(followerId, 0))
            .isNotEmpty
    }

    @Test
    @Transactional
    fun getRecommendPostsTest() {
        //given
        val writerId1 = createWriter1()
        val content1 = """
            개발자들을 위한 물건을 무엇이 있을까?
            개발자들을 위한 장소는 어디가 있을까?
            카페가 개발자들을 위한 장소인것 같다.
        """
        val request1 = CreatePost(writerId1, content1)
        postCommandService.createPost(request1)
        flushAndClear()
        val writerId2 = createWriter2()
        val content2 = "개발자들을 위한 카페"
        val request2 = CreatePost(writerId2, content2)
        postCommandService.createPost(request2)
        flushAndClear()
        val content3 = "교사들을 위한 카페"
        val request3 = CreatePost(writerId2, content3)
        postCommandService.createPost(request3)
        flushAndClear()
        
        //when
        val posts = postQueryService.getRecommendPosts(content1, 0)

        //then
        repeat(posts.size) { logger().info(posts[it]?.content ?: "") }
        Assertions.assertThat(posts).isNotEmpty
    }

    @Test
    @Transactional
    fun getRandomPostsTest() {
        //given
        val writerId1 = createWriter1()
        val request1 = CreatePost(writerId1, "post1")
        postCommandService.createPost(request1)
        flushAndClear()
        val writerId2 = createWriter2()
        val request2 = CreatePost(writerId2, "post2")
        postCommandService.createPost(request2)
        flushAndClear()

        //when
        val posts = postQueryService.getRandomPosts()

        //then
        repeat(posts.size) { logger().info(posts[it].content) }
        Assertions.assertThat(posts).isNotEmpty
    }

    @Test
    @Transactional
    fun getCountOfPostsByWriterTest() {
        //given
        val writerId = createWriter1()
        val countOfRepeatCreatePost = 10
        repeat(countOfRepeatCreatePost) {
            val content = "test_content"
            val request = CreatePost(writerId, content+(it+1))
            postCommandService.createPost(request)
            flushAndClear()
        }

        //when
        val countOfPosts = postQueryService.getCountOfPostsByWriter(writerId)

        //then
        Assertions.assertThat(countOfPosts).isEqualTo(countOfRepeatCreatePost.toLong())
    }
}