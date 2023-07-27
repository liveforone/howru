package howru.howru.post.service.query

import howru.howru.exception.exception.PostException
import howru.howru.logger
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
class PostQueryServiceTest @Autowired constructor(
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
        val request = SignupRequest(email, pw)
        val uuid = memberCommandService.signupMember(request)
        flushAndClear()
        return uuid
    }

    private fun createWriter2(): UUID {
        val email = "test_follower@gmail.com"
        val pw = "2222"
        val request = SignupRequest(email, pw)
        val uuid = memberCommandService.signupMember(request)
        flushAndClear()
        return uuid
    }

    @Test
    @Transactional
    fun getPostByUUIDTest() {
        //given
        val writerUUID = createWriter1()
        val content = "test_content"
        val request = CreatePost(writerUUID, content)
        val postUUID = postCommandService.createPost(request)
        flushAndClear()

        //when
        val post = postQueryService.getPostByUUID(postUUID)

        //then
        Assertions.assertThat(post.content).isEqualTo(content)
    }

    @Test
    @Transactional
    fun getMyPostsTest() {
        //given
        val writerUUID = createWriter1()
        val content1 = "test_content1"
        val request1 = CreatePost(writerUUID, content1)
        postCommandService.createPost(request1)
        flushAndClear()
        val content2 = "test_content2"
        val request2 = CreatePost(writerUUID, content2)
        postCommandService.createPost(request2)
        flushAndClear()

        //when
        val myPosts = postQueryService.getMyPosts(writerUUID, null)

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
        val writerUUID1 = createWriter1()
        val content1 = "test_content1"
        val request1 = CreatePost(writerUUID1, content1)
        postCommandService.createPost(request1)
        flushAndClear()
        val writerUUID2 = createWriter2()
        val content2 = "test_content2"
        val request2 = CreatePost(writerUUID2, content2)
        postCommandService.createPost(request2)
        flushAndClear()

        //when
        val allPosts = postQueryService.getAllPosts(null)

        //then
        Assertions.assertThat(allPosts[0].content).isEqualTo(content2)
    }

    /*
    * 구독하지 않았을때, 상대방의 게시글을 조회하는 경우 예외가 정상적으로 발생하는지 확인하는 테스트
    * PostException 이 발생해야한다.
     */
    @Test
    @Transactional
    fun getPostsBySomeoneWhenNoSubscribeTest() {
        //given
        val followeeUUID = createWriter1()
        memberCommandService.memberLockOn(followeeUUID)
        flushAndClear()
        val content1 = "test_content1"
        val request1 = CreatePost(followeeUUID, content1)
        postCommandService.createPost(request1)
        flushAndClear()
        val followerUUID = createWriter2()

        //then -> error 발생!!
        Assertions.assertThatThrownBy { postQueryService.getPostsBySomeone(followeeUUID, followerUUID, null) }
            .isInstanceOf(PostException::class.java)
    }

    /*
    * 구독 한 경우, 상대방의 게시글을 조회할때 정상적으로 처리되는지 확인하는 테스트
    * 구독확인에 대한 쿼리까지 살펴볼 수 있다.
     */
    @Test
    @Transactional
    fun getPostsBySomeoneWhenSubscribeTest() {
        //given
        val followeeUUID = createWriter1()
        memberCommandService.memberLockOn(followeeUUID)
        flushAndClear()
        val content1 = "test_content1"
        val request1 = CreatePost(followeeUUID, content1)
        postCommandService.createPost(request1)
        flushAndClear()
        val followerUUID = createWriter2()

        //when
        val subscribeRequest = CreateSubscribe(followeeUUID, followerUUID)
        subscribeCommandService.createSubscribe(subscribeRequest)
        flushAndClear()

        //then
        Assertions.assertThat(postQueryService.getPostsBySomeone(followeeUUID, followerUUID, null))
            .isNotEmpty
    }

    @Test
    @Transactional
    fun getPostsOfFolloweeTest() {
        //given
        val followeeUUID = createWriter1()
        val content1 = "test_content1"
        val request1 = CreatePost(followeeUUID, content1)
        postCommandService.createPost(request1)
        flushAndClear()
        val followerUUID = createWriter2()

        //when
        val subscribeRequest = CreateSubscribe(followeeUUID, followerUUID)
        subscribeCommandService.createSubscribe(subscribeRequest)
        flushAndClear()

        //then
        Assertions.assertThat(postQueryService.getPostsOfFollowee(followerUUID, null))
            .isNotEmpty
    }

    @Test
    @Transactional
    fun getRecommendPostsTest() {
        //given
        val writerUUID1 = createWriter1()
        val content1 = """
            개발자들을 위한 물건을 무엇이 있을까?
            개발자들을 위한 장소는 어디가 있을까?
            카페가 개발자들을 위한 장소인것 같다.
        """
        val request1 = CreatePost(writerUUID1, content1)
        postCommandService.createPost(request1)
        flushAndClear()
        val writerUUID2 = createWriter2()
        val content2 = "개발자들을 위한 카페"
        val request2 = CreatePost(writerUUID2, content2)
        postCommandService.createPost(request2)
        flushAndClear()
        val content3 = "교사들을 위한 카페"
        val request3 = CreatePost(writerUUID2, content3)
        postCommandService.createPost(request3)
        flushAndClear()
        
        //when
        val posts = postQueryService.getRecommendPosts(content1)

        //then
        repeat(posts.size) { logger().info(posts[it].content) }
        Assertions.assertThat(posts).isNotEmpty
    }

    @Test
    @Transactional
    fun getRandomPostsTest() {
        //given
        val writerUUID1 = createWriter1()
        val request1 = CreatePost(writerUUID1, "post1")
        postCommandService.createPost(request1)
        flushAndClear()
        val writerUUID2 = createWriter2()
        val request2 = CreatePost(writerUUID2, "post2")
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
    fun countPostsByWriterTest() {
        //given
        val writerUUID = createWriter1()
        val countOfRepeatCreatePost = 10
        repeat(countOfRepeatCreatePost) {
            val content = "test_content"
            val request = CreatePost(writerUUID, content+(it+1))
            postCommandService.createPost(request)
            flushAndClear()
        }

        //when
        val countOfPosts = postQueryService.countPostsByWriter(writerUUID)

        //then
        Assertions.assertThat(countOfPosts).isEqualTo(countOfRepeatCreatePost.toLong())
    }
}