package howru.howru.subscribe.service.query

import howru.howru.member.dto.request.LoginRequest
import howru.howru.member.dto.request.SignupRequest
import howru.howru.member.service.command.MemberCommandService
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
class SubscribeSqlServiceTest @Autowired constructor(
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
        memberCommandService.signupMember(request)
        flushAndClear()
        val loginRequest = LoginRequest(email, pw)
        return memberCommandService.login(loginRequest).uuid
    }

    private fun createFollower(): UUID {
        val email = "test_follower@gmail.com"
        val pw = "3344"
        val nickName = "follower"
        val request = SignupRequest(email, pw, nickName)
        memberCommandService.signupMember(request)
        flushAndClear()
        val loginRequest = LoginRequest(email, pw)
        return memberCommandService.login(loginRequest).uuid
    }

    @Test
    @Transactional
    fun getSubscribesByFollowerTest() {
        //given
        val followeeUUID = createFollowee()
        val followerUUID = createFollower()
        val request = CreateSubscribe(followeeUUID, followerUUID)
        subscribeCommandService.createSubscribe(request)
        flushAndClear()

        //when
        val subscribes = subscribeQueryService.getSubscribesByFollower(followerUUID, null, null)

        //then
        Assertions.assertThat(subscribes[0].followeeUUID).isEqualTo(followeeUUID)
    }

    @Test
    @Transactional
    fun getSubscribesByFollowerPagingTest() {
        //given
        val followeeUUID = createFollowee()
        val followerUUID = createFollower()
        val request = CreateSubscribe(followeeUUID, followerUUID)
        subscribeCommandService.createSubscribe(request)
        flushAndClear()

        //when
        val subscribes = subscribeQueryService.getSubscribesByFollower(followerUUID, followeeUUID, followerUUID)

        //then
        Assertions.assertThat(subscribes).isEmpty()
    }

    @Test
    @Transactional
    fun getCountFollowerTest() {
        //given
        val followeeUUID = createFollowee()
        val followerUUID = createFollower()
        val request = CreateSubscribe(followeeUUID, followerUUID)
        subscribeCommandService.createSubscribe(request)
        flushAndClear()

        //when
        val countFollower = subscribeQueryService.getCountOfFollower(followeeUUID)

        //then
        Assertions.assertThat(countFollower).isEqualTo(1)
    }

    @Test
    @Transactional
    fun getCountFollowerNullTest() {
        /*
        카운트 쿼리는 long 타입으로 조회하는데, 값이 없을경우 즉 카운트가 0일경우
        0으로 값이 리턴되야하는데, 워낙 criteria api가 괴랄해서 0으로 리턴되는지, 아니면 null인지
        아니면 no result exception인지 확인하기 위한 테스트이다.
         */

        //given
        val followeeUUID = createFollowee()
        val followerUUID = createFollower()
        val request = CreateSubscribe(followeeUUID, followerUUID)
        subscribeCommandService.createSubscribe(request)
        flushAndClear()

        //when
        val countFollower = subscribeQueryService.getCountOfFollower(followeeUUID)

        //then
        Assertions.assertThat(countFollower).isEqualTo(1)
    }

    @Test
    @Transactional
    fun isFolloweeTest() {
        //given
        val followeeUUID = createFollowee()
        val followerUUID = createFollower()
        val request = CreateSubscribe(followeeUUID, followerUUID)
        subscribeCommandService.createSubscribe(request)
        flushAndClear()

        //when
        val result = subscribeQueryService.isFollowee(followeeUUID, followerUUID)

        //then
        Assertions.assertThat(result).isTrue()
    }

    @Test
    @Transactional
    fun isFollowEachTest() {
        //given
        val followeeUUID = createFollowee()
        val followerUUID = createFollower()
        val request = CreateSubscribe(followeeUUID, followerUUID)
        subscribeCommandService.createSubscribe(request)
        flushAndClear()

        //when
        val result = subscribeQueryService.isFollowEach(followeeUUID, followerUUID)

        //then
        Assertions.assertThat(result).isFalse()
    }
}