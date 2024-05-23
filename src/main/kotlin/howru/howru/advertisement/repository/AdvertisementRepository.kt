package howru.howru.advertisement.repository

import howru.howru.advertisement.domain.Advertisement
import howru.howru.advertisement.dto.response.AdvertisementInfo
import howru.howru.advertisement.repository.sql.AdvertisementSql
import howru.howru.global.util.getDateDigit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface AdvertisementRepository : JpaRepository<Advertisement, Long>, AdvertisementCustomRepository {
    @Query(AdvertisementSql.RANDOM_AD_QUERY)
    fun findRandomAd(): AdvertisementInfo

    @Modifying(clearAutomatically = true)
    @Query(AdvertisementSql.DELETE_EXPIRED_QUERY)
    fun deleteExpiredAd(
        @Param(AdvertisementSql.DELETE_EXPIRED_PARAM) nowDate: Int =
            getDateDigit(
                LocalDate.now().minusMonths(AdvertisementSql.THREE_MONTH)
            )
    )
}
