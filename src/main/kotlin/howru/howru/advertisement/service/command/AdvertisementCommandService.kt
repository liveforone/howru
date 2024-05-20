package howru.howru.advertisement.service.command

import howru.howru.advertisement.domain.Advertisement
import howru.howru.advertisement.dto.CreateAdvertisement
import howru.howru.advertisement.dto.UpdateAdContent
import howru.howru.advertisement.dto.UpdateAdTitle
import howru.howru.advertisement.log.AdServiceLog
import howru.howru.advertisement.repository.AdvertisementRepository
import howru.howru.advertisement.service.command.constant.AdScheduleConstant
import howru.howru.member.exception.MemberException
import howru.howru.member.exception.MemberExceptionMessage
import howru.howru.logger
import howru.howru.member.repository.MemberCustomRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class AdvertisementCommandService
    @Autowired
    constructor(
        private val advertisementRepository: AdvertisementRepository,
        private val memberRepository: MemberCustomRepository
    ) {
        private fun checkAdmin(memberId: UUID) {
            require(memberRepository.findMemberById(memberId).isAdmin()) {
                logger().warn(AdServiceLog.ACCESS_NON_ADMIN_USER + memberId)
                throw MemberException(MemberExceptionMessage.AUTH_IS_NOT_ADMIN, memberId.toString())
            }
        }

        fun createHalfAd(
            createAdvertisement: CreateAdvertisement,
            memberId: UUID
        ): Long {
            checkAdmin(memberId)
            return with(createAdvertisement) {
                Advertisement.createHalfAd(company!!, title!!, content!!)
                    .run { advertisementRepository.save(this).id!! }
            }
        }

        fun createYearAd(
            createAdvertisement: CreateAdvertisement,
            memberId: UUID
        ): Long {
            checkAdmin(memberId)
            return with(createAdvertisement) {
                Advertisement.createYearAd(company!!, title!!, content!!)
                    .run { advertisementRepository.save(this).id!! }
            }
        }

        fun editAdTitle(
            id: Long,
            updateAdTitle: UpdateAdTitle,
            memberId: UUID
        ) {
            checkAdmin(memberId)
            with(updateAdTitle) {
                advertisementRepository.findAdvertisementById(id).also { it.editTitle(title!!) }
            }
        }

        fun editAdContent(
            id: Long,
            updateAdContent: UpdateAdContent,
            memberId: UUID
        ) {
            checkAdmin(memberId)
            with(updateAdContent) {
                advertisementRepository.findAdvertisementById(id).also { it.editContent(content!!) }
            }
        }

        fun removeAd(
            id: Long,
            memberId: UUID
        ) {
            advertisementRepository.findAdvertisementById(id)
                .takeIf { memberRepository.findMemberById(memberId).isAdmin() }
                ?.also { advertisementRepository.delete(it) }
                ?: run {
                    logger().warn(AdServiceLog.ACCESS_NON_ADMIN_USER + memberId)
                    throw MemberException(MemberExceptionMessage.AUTH_IS_NOT_ADMIN, memberId.toString())
                }
        }

        @Scheduled(cron = AdScheduleConstant.DELETE_EXPIRED_POLICY)
        fun removeExpiredAd() {
            advertisementRepository.deleteExpiredAd()
        }
    }
