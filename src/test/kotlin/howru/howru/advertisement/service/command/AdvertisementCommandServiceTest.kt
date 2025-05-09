package howru.howru.advertisement.service.command

import howru.howru.advertisement.dto.request.CreateAdvertisement
import howru.howru.advertisement.dto.request.UpdateAdvertisement
import howru.howru.advertisement.service.query.AdvertisementQueryService
import howru.howru.advertisement.exception.AdvertisementException
import howru.howru.global.util.getDateDigit
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
class AdvertisementCommandServiceTest
    @Autowired
    constructor(
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
            memberCommandService.signup(request)
            flushAndClear()
            val loginRequest = LoginRequest(email, pw)
            return memberCommandService.login(loginRequest).id
        }

        @Test @Transactional
        fun createHalfAd() {
            // given
            val adminId = createAdmin()
            val company = "test company"
            val title = "test title"
            val content = "create half ad test"
            val request = CreateAdvertisement(company, title, content)

            // when
            val adId = advertisementCommandService.createHalfAd(request, adminId)
            flushAndClear()

            // then
            val ad = advertisementQueryService.getOneById(adId)
            Assertions.assertThat(ad.content).isEqualTo(content)
            Assertions.assertThat(ad.createdDate).isEqualTo(getDateDigit(LocalDate.now()))
            Assertions.assertThat(ad.endDate).isEqualTo(getDateDigit(LocalDate.now().plusMonths(6)))
        }

        @Test @Transactional
        fun createYearAd() {
            // given
            val adminId = createAdmin()
            val company = "test company"
            val title = "test title"
            val content = "create year ad test"
            val request = CreateAdvertisement(company, title, content)

            // when
            val adId = advertisementCommandService.createYearAd(request, adminId)
            flushAndClear()

            // then
            val ad = advertisementQueryService.getOneById(adId)
            Assertions.assertThat(ad.content).isEqualTo(content)
            Assertions.assertThat(ad.createdDate).isEqualTo(getDateDigit(LocalDate.now()))
            Assertions.assertThat(ad.endDate).isEqualTo(getDateDigit(LocalDate.now().plusYears(1)))
        }

        @Test @Transactional
        fun editAd() {
            // given
            val adminId = createAdmin()
            val company = "test company"
            val title = "test title"
            val content = "edit title ad test"
            val request = CreateAdvertisement(company, title, content)
            val adId = advertisementCommandService.createYearAd(request, adminId)
            flushAndClear()

            // when
            val updatedTitle = "updated title"
            val updatedContent = "updated content"
            val updateRequest = UpdateAdvertisement(updatedTitle, updatedContent)
            advertisementCommandService.editAd(adId, updateRequest, adminId)
            flushAndClear()

            // then
            Assertions
                .assertThat(advertisementQueryService.getOneById(adId).title)
                .isEqualTo(updatedTitle)
            Assertions
                .assertThat(advertisementQueryService.getOneById(adId).content)
                .isEqualTo(updatedContent)
        }

        @Test @Transactional
        fun removeAdById() {
            // given
            val adminId = createAdmin()
            val company = "test company"
            val title = "test title"
            val content = "delete ad test"
            val request = CreateAdvertisement(company, title, content)
            val adId = advertisementCommandService.createYearAd(request, adminId)
            flushAndClear()

            // when
            advertisementCommandService.removeAd(adId, adminId)
            flushAndClear()

            // then
            Assertions
                .assertThatThrownBy { advertisementQueryService.getOneById(adId) }
                .isInstanceOf(AdvertisementException::class.java)
        }

        @Test @Transactional
        fun removeExpiredThreeMonthSchedule() {
            // given
            val adminId = createAdmin()
            val company = "test company"
            val title = "test title"
            val content = "delete ad test"
            val request = CreateAdvertisement(company, title, content)
            val adId = advertisementCommandService.createYearAd(request, adminId)
            flushAndClear()

            // when
            advertisementCommandService.removeExpiredAd()
            flushAndClear()

            // when
            Assertions
                .assertThat(advertisementQueryService.getOneById(adId))
                .isNotNull
        }
    }
