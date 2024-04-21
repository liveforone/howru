package howru.howru.advertisement.repository

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import howru.howru.advertisement.domain.Advertisement
import howru.howru.advertisement.domain.QAdvertisement
import howru.howru.advertisement.dto.response.AdvertisementInfo
import howru.howru.exception.exception.AdvertisementException
import howru.howru.exception.message.AdvertisementExceptionMessage
import howru.howru.globalUtil.getDateDigit
import java.time.LocalDate

class AdvertisementCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val advertisement: QAdvertisement = QAdvertisement.advertisement
) : AdvertisementCustomRepository {
    override fun findAdvertisementById(id: Long): Advertisement {
        return jpaQueryFactory.selectFrom(advertisement)
            .where(advertisement.id.eq(id))
            .fetchOne() ?: throw AdvertisementException(AdvertisementExceptionMessage.AD_IS_NULL, id)
    }

    private val advertisementInfoField =
        Projections.constructor(
            AdvertisementInfo::class.java,
            advertisement.id,
            advertisement.company,
            advertisement.title,
            advertisement.content,
            advertisement.createdDate,
            advertisement.endDate
        )

    override fun findAdvertisementInfoById(id: Long): AdvertisementInfo {
        return jpaQueryFactory.select(advertisementInfoField)
            .from(advertisement)
            .where(advertisement.id.eq(id))
            .fetchOne() ?: throw AdvertisementException(AdvertisementExceptionMessage.AD_IS_NULL, id)
    }

    override fun findAllAdvertisements(): List<AdvertisementInfo> {
        return jpaQueryFactory.select(advertisementInfoField)
            .from(advertisement)
            .orderBy(advertisement.id.desc())
            .fetch()
    }

    override fun searchAdByCompany(company: String): List<AdvertisementInfo> {
        return jpaQueryFactory.select(advertisementInfoField)
            .from(advertisement)
            .where(advertisement.company.startsWith(company))
            .orderBy(advertisement.id.desc())
            .fetch()
    }

    override fun findExpiredAds(): List<AdvertisementInfo> {
        return jpaQueryFactory.select(advertisementInfoField)
            .from(advertisement)
            .where(advertisement.endDate.lt(getDateDigit(LocalDate.now())))
            .orderBy(advertisement.id.desc())
            .fetch()
    }
}
