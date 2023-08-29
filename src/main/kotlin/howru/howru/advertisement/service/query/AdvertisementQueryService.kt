package howru.howru.advertisement.service.query

import howru.howru.advertisement.repository.AdvertisementRepository
import howru.howru.globalConfig.cache.constant.CacheName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AdvertisementQueryService @Autowired constructor(
    private val advertisementRepository: AdvertisementRepository
) {
    fun getOneById(id: Long) = advertisementRepository.findOneDtoById(id)
    @Cacheable(cacheNames = [CacheName.ADVERTISEMENT])
    fun getAllAdvertisement() = advertisementRepository.findAllAdvertisement()
    fun searchAdByCompany(company: String) = advertisementRepository.searchAdByCompany(company)
    fun getExpiredAd() = advertisementRepository.findExpiredAd()
    fun getRandomAd() = advertisementRepository.findRandomAd()
}