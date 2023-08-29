package howru.howru.advertisement.service.command

import howru.howru.advertisement.dto.request.CreateAdvertisement
import howru.howru.advertisement.dto.update.UpdateAdContent
import howru.howru.advertisement.dto.update.UpdateAdTitle
import howru.howru.advertisement.service.query.AdvertisementQueryService
import howru.howru.exception.exception.AdvertisementException
import howru.howru.globalUtil.getDateDigit
import howru.howru.member.dto.request.LoginRequest
import howru.howru.member.dto.request.SignupRequest
import howru.howru.member.service.command.MemberCommandService
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.*

@SpringBootTest
class AdvertisementCommandServiceTest @Autowired constructor(
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
        val nickName = "testName"
        val request = SignupRequest(email, pw, nickName)
        memberCommandService.signupMember(request)
        flushAndClear()
        val loginRequest = LoginRequest(email, pw)
        return memberCommandService.login(loginRequest).uuid
    }

    @Test @Transactional
    fun createHalfAd() {
        //given
        val adminUUID = createAdmin()
        val company = "test company"
        val title = "test title"
        val content = "create half ad test"
        val request = CreateAdvertisement(company, title, content)

        //when
        val adId = advertisementCommandService.createHalfAd(request, adminUUID)
        flushAndClear()

        //then
        val ad = advertisementQueryService.getOneById(adId)
        Assertions.assertThat(ad.content).isEqualTo(content)
        Assertions.assertThat(ad.createdDate).isEqualTo(getDateDigit(LocalDate.now()))
        Assertions.assertThat(ad.endDate).isEqualTo(getDateDigit(LocalDate.now().plusMonths(6)))
    }

    @Test @Transactional
    fun createYearAd() {
        //given
        val adminUUID = createAdmin()
        val company = "test company"
        val title = "test title"
        val content = "create year ad test"
        val request = CreateAdvertisement(company, title, content)

        //when
        val adId = advertisementCommandService.createYearAd(request, adminUUID)
        flushAndClear()

        //then
        val ad = advertisementQueryService.getOneById(adId)
        Assertions.assertThat(ad.content).isEqualTo(content)
        Assertions.assertThat(ad.createdDate).isEqualTo(getDateDigit(LocalDate.now()))
        Assertions.assertThat(ad.endDate).isEqualTo(getDateDigit(LocalDate.now().plusYears(1)))
    }

    @Test @Transactional
    fun editTitle() {
        //given
        val adminUUID = createAdmin()
        val company = "test company"
        val title = "test title"
        val content = "edit title ad test"
        val request = CreateAdvertisement(company, title, content)
        val adId = advertisementCommandService.createYearAd(request, adminUUID)
        flushAndClear()

        //when
        val updatedTitle = "updated title"
        val updateRequest = UpdateAdTitle(adId, updatedTitle)
        advertisementCommandService.editTitle(updateRequest, adminUUID)
        flushAndClear()

        //then
        Assertions.assertThat(advertisementQueryService.getOneById(adId).title)
            .isEqualTo(updatedTitle)
    }

    @Test @Transactional
    fun editContent() {
        //given
        val adminUUID = createAdmin()
        val company = "test company"
        val title = "test title"
        val content = "edit content ad test"
        val request = CreateAdvertisement(company, title, content)
        val adId = advertisementCommandService.createYearAd(request, adminUUID)
        flushAndClear()

        //when
        val updatedContent = "updated content"
        val updateRequest = UpdateAdContent(adId, updatedContent)
        advertisementCommandService.editContent(updateRequest, adminUUID)
        flushAndClear()

        //then
        Assertions.assertThat(advertisementQueryService.getOneById(adId).content)
            .isEqualTo(updatedContent)
    }

    @Test @Transactional
    fun deleteAdByUUID() {
        //given
        val adminUUID = createAdmin()
        val company = "test company"
        val title = "test title"
        val content = "delete ad test"
        val request = CreateAdvertisement(company, title, content)
        val adId = advertisementCommandService.createYearAd(request, adminUUID)
        flushAndClear()

        //when
        advertisementCommandService.deleteAdByUUID(adId, adminUUID)
        flushAndClear()

        //then
        Assertions.assertThatThrownBy { advertisementQueryService.getOneById(adId) }
            .isInstanceOf(AdvertisementException::class.java)
    }

    @Test @Transactional
    fun deleteExpiredThreeMonthSchedule() {
        //given
        val adminUUID = createAdmin()
        val company = "test company"
        val title = "test title"
        val content = "delete ad test"
        val request = CreateAdvertisement(company, title, content)
        val adId = advertisementCommandService.createYearAd(request, adminUUID)
        flushAndClear()

        //when
        advertisementCommandService.deleteExpiredThreeMonthSchedule()
        flushAndClear()

        //when
        Assertions.assertThat(advertisementQueryService.getOneById(adId))
            .isNotNull
    }
}