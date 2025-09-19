package howru.howru.advertisement.domain

import howru.howru.advertisement.domain.constant.AdvertisementConstant
import howru.howru.global.util.getDateDigit
import jakarta.persistence.*
import java.time.LocalDate

@Entity
class Advertisement private constructor(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @Column(nullable = false) val company: String,
    @Column(nullable = false) var title: String,
    @Column(nullable = false, columnDefinition = AdvertisementConstant.CONTENT_TYPE) var content: String,
    @Column(nullable = false, updatable = false) val createdDate: Int =
        getDateDigit(LocalDate.now()),
    @Column(nullable = false, updatable = false) val endDate: Int
) {
    companion object {
        fun createHalfAd(
            company: String,
            title: String,
            content: String
        ): Advertisement =
            Advertisement(
                company = company,
                title = title,
                content = content,
                endDate = getDateDigit(LocalDate.now().plusMonths(AdvertisementConstant.HALF_MONTH_TYPE))
            )

        fun createYearAd(
            company: String,
            title: String,
            content: String
        ): Advertisement =
            Advertisement(
                company = company,
                title = title,
                content = content,
                endDate = getDateDigit(LocalDate.now().plusYears(AdvertisementConstant.ONE_YEAR_TYPE))
            )
    }

    fun editTitle(title: String) {
        this.title = title
    }

    fun editContent(content: String) {
        this.content = content
    }
}
