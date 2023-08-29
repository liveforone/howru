package howru.howru.advertisement.service.command

import howru.howru.advertisement.domain.Advertisement
import howru.howru.advertisement.dto.request.CreateAdvertisement
import howru.howru.advertisement.dto.update.UpdateAdContent
import howru.howru.advertisement.dto.update.UpdateAdTitle
import howru.howru.advertisement.repository.AdvertisementRepository
import howru.howru.advertisement.service.command.constant.AdScheduleConstant
import howru.howru.exception.exception.AdvertisementException
import howru.howru.exception.message.AdvertisementExceptionMessage
import howru.howru.globalConfig.cache.constant.CacheName
import howru.howru.member.repository.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class AdvertisementCommandService @Autowired constructor(
    private val advertisementRepository: AdvertisementRepository,
    private val memberRepository: MemberRepository
) {
    @CacheEvict(cacheNames = [CacheName.ADVERTISEMENT])
    fun createHalfAd(createAdvertisement: CreateAdvertisement, memberUUID: UUID): Long {
        require(memberRepository.findOneByUUID(memberUUID).isAdmin()) { throw AdvertisementException(AdvertisementExceptionMessage.NOT_ADMIN) }
        return with(createAdvertisement) {
            Advertisement.createHalfAd(company!!, title!!, content!!)
                .run { advertisementRepository.save(this).id!! }
        }
    }

    @CacheEvict(cacheNames = [CacheName.ADVERTISEMENT])
    fun createYearAd(createAdvertisement: CreateAdvertisement, memberUUID: UUID): Long {
        require(memberRepository.findOneByUUID(memberUUID).isAdmin()) { throw AdvertisementException(AdvertisementExceptionMessage.NOT_ADMIN) }
        return with(createAdvertisement) {
            Advertisement.createYearAd(company!!, title!!, content!!)
                .run { advertisementRepository.save(this).id!! }
        }
    }

    @CacheEvict(cacheNames = [CacheName.ADVERTISEMENT])
    fun editTitle(updateAdTitle: UpdateAdTitle, memberUUID: UUID) {
        require(memberRepository.findOneByUUID(memberUUID).isAdmin()) { throw AdvertisementException(AdvertisementExceptionMessage.NOT_ADMIN) }
        with(updateAdTitle) {
            advertisementRepository.findOneById(id!!)
                .also { it.editTitle(title!!) }
        }
    }

    @CacheEvict(cacheNames = [CacheName.ADVERTISEMENT])
    fun editContent(updateAdContent: UpdateAdContent, memberUUID: UUID) {
        require(memberRepository.findOneByUUID(memberUUID).isAdmin()) { throw AdvertisementException(AdvertisementExceptionMessage.NOT_ADMIN) }
        with(updateAdContent) {
            advertisementRepository.findOneById(id!!)
                .also { it.editContent(content!!) }
        }
    }

    @CacheEvict(cacheNames = [CacheName.ADVERTISEMENT])
    fun deleteAdByUUID(id: Long, memberUUID: UUID) {
        advertisementRepository.findOneById(id)
            .takeIf { memberRepository.findOneByUUID(memberUUID).isAdmin() }
            ?.also { advertisementRepository.delete(it) }
            ?: throw AdvertisementException(AdvertisementExceptionMessage.NOT_ADMIN)
    }

    @Scheduled(cron = AdScheduleConstant.DELETE_EXPIRED_POLICY)
    @CacheEvict(cacheNames = [CacheName.ADVERTISEMENT])
    fun deleteExpiredThreeMonthSchedule() {
        advertisementRepository.deleteExpiredThreeMonthAd()
    }
}