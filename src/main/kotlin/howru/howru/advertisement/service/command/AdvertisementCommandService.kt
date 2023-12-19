package howru.howru.advertisement.service.command

import howru.howru.advertisement.domain.Advertisement
import howru.howru.advertisement.dto.request.CreateAdvertisement
import howru.howru.advertisement.dto.request.UpdateAdContent
import howru.howru.advertisement.dto.request.UpdateAdTitle
import howru.howru.advertisement.log.AdServiceLog
import howru.howru.advertisement.repository.AdvertisementQuery
import howru.howru.advertisement.repository.AdvertisementRepository
import howru.howru.advertisement.service.command.constant.AdScheduleConstant
import howru.howru.exception.exception.MemberException
import howru.howru.exception.message.MemberExceptionMessage
import howru.howru.globalConfig.cache.constant.CacheName
import howru.howru.logger
import howru.howru.member.repository.MemberQuery
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
    private val advertisementQuery: AdvertisementQuery,
    private val memberQuery: MemberQuery
) {
    @CacheEvict(cacheNames = [CacheName.ADVERTISEMENT])
    fun createHalfAd(createAdvertisement: CreateAdvertisement, memberUUID: UUID): Long {
        require(memberQuery.findOneByUUID(memberUUID).isAdmin()) {
            logger().warn(AdServiceLog.ACCESS_NON_ADMIN_USER + memberUUID)
            throw MemberException(MemberExceptionMessage.NOT_ADMIN, memberUUID.toString())
        }
        return with(createAdvertisement) {
            Advertisement.createHalfAd(company!!, title!!, content!!)
                .run { advertisementRepository.save(this).id!! }
        }
    }

    @CacheEvict(cacheNames = [CacheName.ADVERTISEMENT])
    fun createYearAd(createAdvertisement: CreateAdvertisement, memberUUID: UUID): Long {
        require(memberQuery.findOneByUUID(memberUUID).isAdmin()) {
            logger().warn(AdServiceLog.ACCESS_NON_ADMIN_USER + memberUUID)
            throw MemberException(MemberExceptionMessage.NOT_ADMIN, memberUUID.toString())
        }
        return with(createAdvertisement) {
            Advertisement.createYearAd(company!!, title!!, content!!)
                .run { advertisementRepository.save(this).id!! }
        }
    }

    @CacheEvict(cacheNames = [CacheName.ADVERTISEMENT])
    fun editTitle(id: Long, updateAdTitle: UpdateAdTitle, memberUUID: UUID) {
        require(memberQuery.findOneByUUID(memberUUID).isAdmin()) {
            logger().warn(AdServiceLog.ACCESS_NON_ADMIN_USER + memberUUID)
            throw MemberException(MemberExceptionMessage.NOT_ADMIN, memberUUID.toString())
        }
        with(updateAdTitle) {
            advertisementQuery.findOneById(id)
                .also { it.editTitle(title!!) }
        }
    }

    @CacheEvict(cacheNames = [CacheName.ADVERTISEMENT])
    fun editContent(id: Long, updateAdContent: UpdateAdContent, memberUUID: UUID) {
        require(memberQuery.findOneByUUID(memberUUID).isAdmin()) {
            logger().warn(AdServiceLog.ACCESS_NON_ADMIN_USER + memberUUID)
            throw MemberException(MemberExceptionMessage.NOT_ADMIN, memberUUID.toString())
        }
        with(updateAdContent) {
            advertisementQuery.findOneById(id)
                .also { it.editContent(content!!) }
        }
    }

    @CacheEvict(cacheNames = [CacheName.ADVERTISEMENT])
    fun removeAdById(id: Long, memberUUID: UUID) {
        advertisementQuery.findOneById(id)
            .takeIf { memberQuery.findOneByUUID(memberUUID).isAdmin() }
            ?.also { advertisementRepository.delete(it) }
            ?: run { logger().warn(AdServiceLog.ACCESS_NON_ADMIN_USER + memberUUID); throw MemberException(MemberExceptionMessage.NOT_ADMIN, memberUUID.toString()) }
    }

    @Scheduled(cron = AdScheduleConstant.DELETE_EXPIRED_POLICY)
    @CacheEvict(cacheNames = [CacheName.ADVERTISEMENT])
    fun removeExpiredThreeMonthSchedule() {
        advertisementRepository.deleteExpiredThreeMonthAd()
    }
}