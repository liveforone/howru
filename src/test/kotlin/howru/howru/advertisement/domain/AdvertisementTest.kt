package howru.howru.advertisement.domain

import howru.howru.globalUtil.getDateDigit
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate

class AdvertisementTest {
    @Test
    fun createHalfAdTest() {
        // given
        val company = "test company"
        val title = "test half ad"
        val content = "half ad test"

        // when
        val ad = Advertisement.createHalfAd(company, title, content)

        // then
        Assertions.assertThat(ad.endDate).isEqualTo(getDateDigit(LocalDate.now().plusMonths(6)))
    }

    @Test
    fun createYearAdTest() {
        // given
        val company = "test company"
        val title = "test year ad"
        val content = "year ad test"

        // when
        val ad = Advertisement.createYearAd(company, title, content)

        // then
        Assertions.assertThat(ad.endDate).isEqualTo(getDateDigit(LocalDate.now().plusYears(1)))
    }

    @Test
    fun editTitleTest() {
        // given
        val company = "test company"
        val title = "test title edit"
        val content = "edit title"
        val ad = Advertisement.createYearAd(company, title, content)

        // when
        val updatedTitle = "updated ad title"
        ad.editTitle(updatedTitle)

        // then
        Assertions.assertThat(ad.title).isEqualTo(updatedTitle)
    }

    @Test
    fun editContentTest() {
        // given
        val company = "test company"
        val title = "test content edit"
        val content = "edit content"
        val ad = Advertisement.createYearAd(company, title, content)

        // when
        val updatedContent = "updated ad content"
        ad.editContent(updatedContent)

        // then
        Assertions.assertThat(ad.content).isEqualTo(updatedContent)
    }
}
