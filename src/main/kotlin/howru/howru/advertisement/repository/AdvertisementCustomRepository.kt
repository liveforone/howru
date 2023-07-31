package howru.howru.advertisement.repository

import howru.howru.advertisement.domain.Advertisement
import howru.howru.advertisement.dto.response.AdvertisementInfo
import java.util.UUID

interface AdvertisementCustomRepository {
    fun findOneByUUID(uuid: UUID): Advertisement
    fun findOneDtoByUUID(uuid: UUID): AdvertisementInfo
    fun findAllAdvertisement(): List<AdvertisementInfo>
    fun searchAdByCompany(company: String): List<AdvertisementInfo>
    fun findExpiredAd(): List<AdvertisementInfo>
}