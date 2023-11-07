package howru.howru.advertisement.repository

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import howru.howru.advertisement.domain.Advertisement
import howru.howru.advertisement.dto.response.AdvertisementInfo
import howru.howru.advertisement.repository.constant.AdvertisementQuery
import howru.howru.globalUtil.getDateDigit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface AdvertisementRepository : JpaRepository<Advertisement, Long>, KotlinJdslJpqlExecutor {

    @Query(AdvertisementQuery.RANDOM_AD_QUERY)
    fun findRandomAd(): AdvertisementInfo

    @Modifying(clearAutomatically = true)
    @Query(AdvertisementQuery.DELETE_EXPIRED_QUERY)
    fun deleteExpiredThreeMonthAd(
        @Param(AdvertisementQuery.DELETE_EXPIRED_PARAM) nowDate: Int = getDateDigit(
            LocalDate.now().minusMonths(AdvertisementQuery.THREE_MONTH)
        )
    )
}