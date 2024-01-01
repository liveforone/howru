package howru.howru.advertisement.service.query

import howru.howru.advertisement.repository.AdvertisementQuery
import howru.howru.advertisement.repository.AdvertisementRepository
import howru.howru.globalConfig.cache.constant.CacheName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AdvertisementQueryService @Autowired constructor(
    private val advertisementQuery: AdvertisementQuery,
    private val advertisementRepository: AdvertisementRepository
) {
    fun getOneById(id: Long) = advertisementQuery.findOneDtoById(id)
    @Cacheable(cacheNames = [CacheName.ADVERTISEMENT])
    fun getAllAdvertisements() = advertisementQuery.findAllAdvertisements()
    fun searchAdByCompany(company: String) = advertisementQuery.searchAdByCompany(company)
    fun getExpiredAds() = advertisementQuery.findExpiredAds()
    fun getRandomAd() = advertisementRepository.findRandomAd()
}