package howru.howru.advertisement.service.query

import howru.howru.advertisement.repository.AdvertisementRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AdvertisementQueryService
    @Autowired
    constructor(
        private val advertisementRepository: AdvertisementRepository
    ) {
        fun getOneById(id: Long) = advertisementRepository.findAdvertisementInfoById(id)

        fun getAllAdvertisements() = advertisementRepository.findAllAdvertisements()

        fun searchAdByCompany(company: String) = advertisementRepository.searchAdByCompany(company)

        fun getExpiredAds() = advertisementRepository.findExpiredAds()

        fun getRandomAd() = advertisementRepository.findRandomAd()
    }
