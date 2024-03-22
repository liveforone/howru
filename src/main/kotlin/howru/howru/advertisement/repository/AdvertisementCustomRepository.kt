package howru.howru.advertisement.repository

import howru.howru.advertisement.domain.Advertisement
import howru.howru.advertisement.dto.response.AdvertisementInfo

interface AdvertisementCustomRepository {
    fun findAdvertisementById(id: Long): Advertisement
    fun findAdvertisementInfoById(id: Long): AdvertisementInfo
    fun findAllAdvertisements(): List<AdvertisementInfo>
    fun searchAdByCompany(company: String): List<AdvertisementInfo>
    fun findExpiredAds(): List<AdvertisementInfo>
}