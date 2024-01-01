package howru.howru.advertisement.repository

import howru.howru.advertisement.domain.Advertisement
import howru.howru.advertisement.dto.response.AdvertisementInfo
import howru.howru.exception.exception.AdvertisementException
import howru.howru.exception.message.AdvertisementExceptionMessage
import howru.howru.globalUtil.getDateDigit
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class AdvertisementQuery @Autowired constructor(
    private val advertisementRepository: AdvertisementRepository
) {
    fun findOneById(id: Long): Advertisement {
        return advertisementRepository.findAll {
            select(entity(Advertisement::class))
                .from(entity(Advertisement::class))
                .where(path(Advertisement::id).eq(id))
        }.firstOrNull() ?: throw AdvertisementException(AdvertisementExceptionMessage.AD_IS_NULL, id)
    }

    fun findOneDtoById(id: Long): AdvertisementInfo {
        return advertisementRepository.findAll {
            selectNew<AdvertisementInfo>(
                path(Advertisement::id),
                path(Advertisement::company),
                path(Advertisement::title),
                path(Advertisement::content),
                path(Advertisement::createdDate),
                path(Advertisement::endDate)
            ).from(entity(Advertisement::class))
                .where(path(Advertisement::id).eq(id))
        }.firstOrNull() ?: throw AdvertisementException(AdvertisementExceptionMessage.AD_IS_NULL, id)
    }

    fun findAllAdvertisements(): List<AdvertisementInfo?> {
        return advertisementRepository.findAll {
            selectNew<AdvertisementInfo>(
                path(Advertisement::id),
                path(Advertisement::company),
                path(Advertisement::title),
                path(Advertisement::content),
                path(Advertisement::createdDate),
                path(Advertisement::endDate)
            ).from(entity(Advertisement::class))
                .orderBy(path(Advertisement::id).desc())
        }
    }

    fun searchAdByCompany(company: String): List<AdvertisementInfo?> {
        return advertisementRepository.findAll {
            selectNew<AdvertisementInfo>(
                path(Advertisement::id),
                path(Advertisement::company),
                path(Advertisement::title),
                path(Advertisement::content),
                path(Advertisement::createdDate),
                path(Advertisement::endDate)
            ).from(entity(Advertisement::class))
                .where(path(Advertisement::company).like("$company%"))
                .orderBy(path(Advertisement::id).desc())
        }
    }

    fun findExpiredAds(): List<AdvertisementInfo?> {
        return advertisementRepository.findAll {
            selectNew<AdvertisementInfo>(
                path(Advertisement::id),
                path(Advertisement::company),
                path(Advertisement::title),
                path(Advertisement::content),
                path(Advertisement::createdDate),
                path(Advertisement::endDate)
            ).from(entity(Advertisement::class))
                .where(path(Advertisement::endDate).lessThan(getDateDigit(LocalDate.now())))
                .orderBy(path(Advertisement::id).desc())
        }
    }
}