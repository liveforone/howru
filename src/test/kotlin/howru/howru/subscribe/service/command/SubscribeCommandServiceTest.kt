package howru.howru.subscribe.service.command

import howru.howru.member.dto.request.LoginRequest
import howru.howru.member.dto.request.SignupRequest
import howru.howru.member.service.command.MemberCommandService
import howru.howru.subscribe.dto.request.CreateSubscribe
import howru.howru.subscribe.dto.request.UnsubscribeRequest
import howru.howru.subscribe.service.query.SubscribeQueryService
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@SpringBootTest
class SubscribeCommandServiceTest @Autowired constructor(
    private val entityManager: EntityManager,
    private val memberCommandService: MemberCommandService,
    private val subscribeCommandService: SubscribeCommandService,
    private val subscribeQueryService: SubscribeQueryService
) {

    private fun flushAndClear() {
        entityManager.flush()
        entityManager.clear()
    }

    private fun createFollowee(): UUID {
        val email = "test_followee@gmail.com"
        val pw = "1122"
        val nickName = "followee"
        val request = SignupRequest(email, pw, nickName)
        memberCommandService.signup(request)
        flushAndClear()
        val loginRequest = LoginRequest(email, pw)
        return memberCommandService.login(loginRequest).id
    }

    private fun createFollower(): UUID {
        val email = "test_follower@gmail.com"
        val pw = "3344"
        val nickName = "follower"
        val request = SignupRequest(email, pw, nickName)
        memberCommandService.signup(request)
        flushAndClear()
        val loginRequest = LoginRequest(email, pw)
        return memberCommandService.login(loginRequest).id
    }

    @Test
    @Transactional
    fun createSubscribeTest() {
        //given
        val followeeId = createFollowee()
        val followerId = createFollower()

        //when
        val request = CreateSubscribe(followeeId, followerId)
        subscribeCommandService.createSubscribe(request)
        flushAndClear()

        //then
        Assertions.assertThat(subscribeQueryService.getSubscribesByFollower(followerId, null)[0].followeeId)
            .isEqualTo(followeeId)
    }

    @Test
    @Transactional
    fun unsubscribeTest() {
        //given
        val followeeId = createFollowee()
        val followerId = createFollower()
        val request = CreateSubscribe(followeeId, followerId)
        subscribeCommandService.createSubscribe(request)
        flushAndClear()

        //when
        val unsubscribeRequest = UnsubscribeRequest(followeeId, followerId)
        subscribeCommandService.unsubscribe(unsubscribeRequest)
        flushAndClear()

        //then
        Assertions.assertThat(subscribeQueryService.getSubscribesByFollower(followerId, null))
            .isEmpty()
    }
}