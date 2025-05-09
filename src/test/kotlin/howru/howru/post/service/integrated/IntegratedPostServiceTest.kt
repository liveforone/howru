package howru.howru.post.service.integrated

import howru.howru.member.dto.request.LoginRequest
import howru.howru.member.dto.request.SignupRequest
import howru.howru.member.service.command.MemberCommandService
import howru.howru.post.dto.request.CreatePost
import howru.howru.post.service.command.PostCommandService
import howru.howru.subscribe.dto.request.CreateSubscribe
import howru.howru.subscribe.exception.SubscribeException
import howru.howru.subscribe.service.command.SubscribeCommandService
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.util.*

@SpringBootTest
class IntegratedPostServiceTest
    @Autowired
    constructor(
        private val entityManager: EntityManager,
        private val memberCommandService: MemberCommandService,
        private val subscribeCommandService: SubscribeCommandService,
        private val postCommandService: PostCommandService,
        private val integratedPostService: IntegratedPostService
    ) {
        private fun flushAndClear() {
            entityManager.flush()
            entityManager.clear()
        }

        private fun createMember1(): UUID {
            val email = "test_member1@gmail.com"
            val pw = "1111"
            val nickName = "member1"
            val request = SignupRequest(email, pw, nickName)
            memberCommandService.signup(request)
            flushAndClear()
            val loginRequest = LoginRequest(email, pw)
            return memberCommandService.login(loginRequest).id
        }

        private fun createMember2(): UUID {
            val email = "test_member2@gmail.com"
            val pw = "2222"
            val nickName = "member2"
            val request = SignupRequest(email, pw, nickName)
            memberCommandService.signup(request)
            flushAndClear()
            val loginRequest = LoginRequest(email, pw)
            return memberCommandService.login(loginRequest).id
        }

    /*
     * 구독하지 않았을때, 상대방의 게시글을 조회하는 경우 예외가 정상적으로 발생하는지 확인하는 테스트
     * PostException 이 발생해야한다.
     */
        @Test
        @Transactional
        fun getPostsOfOtherMemberWhenNoSubscribeTest() {
            // given
            val followeeId = createMember1()
            memberCommandService.memberLockOn(followeeId)
            flushAndClear()
            val content1 = "test_content"
            val request1 = CreatePost(followeeId, content1)
            postCommandService.createPost(request1)
            flushAndClear()
            val followerId = createMember2()

            // then -> error 발생!!
            Assertions
                .assertThatThrownBy {
                    integratedPostService.getPostOfOtherMember(followeeId, followerId, null)
                }.isInstanceOf(SubscribeException::class.java)
        }

    /*
     * 구독 한 경우, 상대방의 게시글을 조회할때 정상적으로 처리되는지 확인하는 테스트
     * 구독확인에 대한 쿼리까지 살펴볼 수 있다.
     * 에러가 발생하지 않는다면 성공이다.
     */
        @Test
        @Transactional
        fun getPostsOfOtherMemberWhenSubscribeTest() {
            // given
            val followeeId = createMember1()
            memberCommandService.memberLockOn(followeeId)
            flushAndClear()
            val content1 = "test_content"
            val request1 = CreatePost(followeeId, content1)
            postCommandService.createPost(request1)
            flushAndClear()
            val followerId = createMember2()

            // when
            val subscribeRequest = CreateSubscribe(followeeId, followerId)
            subscribeCommandService.createSubscribe(subscribeRequest)
            flushAndClear()

            // then
            Assertions
                .assertThat(
                    integratedPostService.getPostOfOtherMember(followeeId, followerId, null).postInfoList
                ).isNotEmpty
        }

        @Test
        @Transactional
        fun getPostsOfFolloweeTest() {
            // given
            val followeeId = createMember1()
            val content1 = "test_content1"
            val request1 = CreatePost(followeeId, content1)
            postCommandService.createPost(request1)
            flushAndClear()
            val followerId = createMember2()

            // when
            val subscribeRequest = CreateSubscribe(followeeId, followerId)
            subscribeCommandService.createSubscribe(subscribeRequest)
            flushAndClear()

            // then
            Assertions
                .assertThat(integratedPostService.getPostsOfFollowee(followerId, null).postInfoList)
                .isNotEmpty
        }
    }
