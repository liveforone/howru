package howru.howru.advertisement.repository

import howru.howru.advertisement.domain.Advertisement
import howru.howru.advertisement.dto.response.AdvertisementInfo

interface AdvertisementCustomRepository {
    fun findOneById(id: Long): Advertisement
    fun findOneDtoById(id: Long): AdvertisementInfo
    fun findAllAdvertisements(): List<AdvertisementInfo>
    fun searchAdByCompany(company: String): List<AdvertisementInfo>
    fun findExpiredAds(): List<AdvertisementInfo>
}