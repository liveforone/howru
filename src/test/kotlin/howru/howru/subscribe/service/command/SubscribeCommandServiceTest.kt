package howru.howru.subscribe.service.command

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
        val request = SignupRequest(email, pw)
        val uuid = memberCommandService.signupMember(request)
        flushAndClear()
        return uuid
    }

    private fun createFollower(): UUID {
        val email = "test_follower@gmail.com"
        val pw = "3344"
        val request = SignupRequest(email, pw)
        val uuid = memberCommandService.signupMember(request)
        flushAndClear()
        return uuid
    }

    @Test
    @Transactional
    fun createSubscribe() {
        //given
        val followeeUUID = createFollowee()
        val followerUUID = createFollower()

        //when
        val request = CreateSubscribe(followeeUUID, followerUUID)
        subscribeCommandService.createSubscribe(request)
        flushAndClear()

        //then
        Assertions.assertThat(subscribeQueryService.getSubscribesByFollower(followerUUID)[0].followeeUUID)
            .isEqualTo(followeeUUID)
    }

    @Test
    @Transactional
    fun unsubscribe() {
        //given
        val followeeUUID = createFollowee()
        val followerUUID = createFollower()
        val request = CreateSubscribe(followeeUUID, followerUUID)
        subscribeCommandService.createSubscribe(request)
        flushAndClear()

        //when
        val unsubscribeRequest = UnsubscribeRequest(followeeUUID, followerUUID)
        subscribeCommandService.unsubscribe(unsubscribeRequest)
        flushAndClear()

        //then
        Assertions.assertThat(subscribeQueryService.getSubscribesByFollower(followerUUID))
            .isEmpty()
    }
}