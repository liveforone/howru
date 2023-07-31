package howru.howru.advertisement.service.query

import howru.howru.advertisement.dto.request.CreateAdvertisement
import howru.howru.advertisement.service.command.AdvertisementCommandService
import howru.howru.member.dto.request.SignupRequest
import howru.howru.member.service.command.MemberCommandService
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.util.*

@SpringBootTest
class AdvertisementQueryServiceTest @Autowired constructor(
    private val entityManager: EntityManager,
    private val memberCommandService: MemberCommandService,
    private val advertisementCommandService: AdvertisementCommandService,
    private val advertisementQueryService: AdvertisementQueryService
) {

    private fun flushAndClear() {
        entityManager.flush()
        entityManager.clear()
    }

    private fun createAdmin(): UUID {
        val email = "admin_howru@gmail.com"
        val pw = "1122"
        val request = SignupRequest(email, pw)
        val uuid = memberCommandService.signupMember(request)
        flushAndClear()
        return uuid
    }

    @Test @Transactional
    fun getOneByUUID() {
        //given
        val adminUUID = createAdmin()
        val company = "test company"
        val title = "test title"
        val content = "test content"
        val request = CreateAdvertisement(company, title, content)
        val adUUID = advertisementCommandService.createHalfAd(request, adminUUID)
        flushAndClear()

        //when
        val ad = advertisementQueryService.getOneByUUID(adUUID)

        //then
        Assertions.assertThat(ad.content).isEqualTo(content)
    }

    @Test @Transactional
    fun getAllAdvertisement() {
        //given
        val adminUUID = createAdmin()
        val company = "test company"
        val title = "test title"
        val content = "test content"
        repeat(4) {
            val request = CreateAdvertisement(company + (it + 1), title + (it + 1), content + (it + 1))
            advertisementCommandService.createHalfAd(request, adminUUID)
            flushAndClear()
        }

        ///when
        val allAd = advertisementQueryService.getAllAdvertisement()

        //then
        //id desc 로 조회하기 때문에 앞쪽 값일 수록 최근값이라 뒤에 붙는 숫자가 늘어난다.
        Assertions.assertThat(allAd[0].content).isEqualTo(content + 4)
    }

    @Test @Transactional
    fun searchAdByCompany() {
        //given
        val adminUUID = createAdmin()
        val company = "test company"
        val title = "test title"
        val content = "test content"
        repeat(4) {
            val request = CreateAdvertisement(company + (it + 1), title + (it + 1), content + (it + 1))
            advertisementCommandService.createHalfAd(request, adminUUID)
            flushAndClear()
        }

        //when
        val adList = advertisementQueryService.searchAdByCompany("test company")

        //then
        Assertions.assertThat(adList.size).isEqualTo(4)
    }

    @Test @Transactional
    fun getExpiredAd() {
        //given
        val adminUUID = createAdmin()
        val company = "test company"
        val title = "test title"
        val content = "test content"
        repeat(4) {
            val request = CreateAdvertisement(company + (it + 1), title + (it + 1), content + (it + 1))
            advertisementCommandService.createHalfAd(request, adminUUID)
            flushAndClear()
        }

        //when
        val adList = advertisementQueryService.getExpiredAd()

        //then
        Assertions.assertThat(adList).isEmpty()
    }

    @Test @Transactional
    fun getRandomAd() {
        //given
        val adminUUID = createAdmin()
        val company = "test company"
        val title = "test title"
        val content = "test content"
        repeat(4) {
            val request = CreateAdvertisement(company + (it + 1), title + (it + 1), content + (it + 1))
            advertisementCommandService.createHalfAd(request, adminUUID)
            flushAndClear()
        }

        //when
        val randomAd = advertisementQueryService.getRandomAd()

        //then
        Assertions.assertThat(randomAd.company).contains(company)
    }
}