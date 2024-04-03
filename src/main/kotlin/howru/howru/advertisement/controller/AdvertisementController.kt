package howru.howru.advertisement.controller

import howru.howru.advertisement.controller.constant.AdvertisementParam
import howru.howru.advertisement.controller.constant.AdvertisementUrl
import howru.howru.advertisement.controller.response.AdvertisementResponse
import howru.howru.advertisement.dto.request.CreateAdvertisement
import howru.howru.advertisement.dto.request.UpdateAdContent
import howru.howru.advertisement.dto.request.UpdateAdTitle
import howru.howru.advertisement.dto.response.AdvertisementInfo
import howru.howru.advertisement.log.AdControllerLog
import howru.howru.advertisement.service.command.AdvertisementCommandService
import howru.howru.advertisement.service.query.AdvertisementQueryService
import howru.howru.logger
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import java.util.UUID

@RestController
class
AdvertisementController
    @Autowired
    constructor(
        private val advertisementQueryService: AdvertisementQueryService,
        private val advertisementCommandService: AdvertisementCommandService
    ) {
        @GetMapping(AdvertisementUrl.DETAIL)
        fun getAdDetailInfo(
            @PathVariable(AdvertisementParam.ID) @Positive id: Long
        ): ResponseEntity<AdvertisementInfo> {
            val ad = advertisementQueryService.getOneById(id)
            return AdvertisementResponse.detailSuccess(ad)
        }

        @GetMapping(AdvertisementUrl.ALL_AD)
        fun getAllAdPage(): ResponseEntity<List<AdvertisementInfo?>> {
            val ads = advertisementQueryService.getAllAdvertisements()
            return AdvertisementResponse.allAdSuccess(ads)
        }

        @GetMapping(AdvertisementUrl.SEARCH_COMPANY)
        fun getSearchCompanyPage(
            @RequestParam(AdvertisementParam.COMPANY) company: String
        ): ResponseEntity<List<AdvertisementInfo?>> {
            val ads = advertisementQueryService.searchAdByCompany(company)
            return AdvertisementResponse.searchAdByCompanySuccess(ads)
        }

        @GetMapping(AdvertisementUrl.EXPIRED_AD)
        fun getExpiredAdPage(): ResponseEntity<List<AdvertisementInfo?>> {
            val ads = advertisementQueryService.getExpiredAds()
            return AdvertisementResponse.expiredAdSuccess(ads)
        }

        @GetMapping(AdvertisementUrl.RANDOM_AD)
        fun getRandomAdInfo(): ResponseEntity<AdvertisementInfo> {
            val ad = advertisementQueryService.getRandomAd()
            return AdvertisementResponse.randomAdSuccess(ad)
        }

        @PostMapping(AdvertisementUrl.CREATE_HALF_AD)
        fun createHalfAd(
            @RequestBody @Valid createAdvertisement: CreateAdvertisement,
            principal: Principal
        ): ResponseEntity<String> {
            advertisementCommandService.createHalfAd(createAdvertisement, UUID.fromString(principal.name))
            logger().info(AdControllerLog.CREATE_SUCCESS + createAdvertisement.company)

            return AdvertisementResponse.createHalfAdSuccess()
        }

        @PostMapping(AdvertisementUrl.CREATE_YEAR_AD)
        fun createYearAd(
            @RequestBody @Valid createAdvertisement: CreateAdvertisement,
            principal: Principal
        ): ResponseEntity<String> {
            advertisementCommandService.createYearAd(createAdvertisement, UUID.fromString(principal.name))
            logger().info(AdControllerLog.CREATE_SUCCESS + createAdvertisement.company)

            return AdvertisementResponse.createYearAdSuccess()
        }

        @PatchMapping(AdvertisementUrl.EDIT_TITLE)
        fun editAdTitle(
            @PathVariable(AdvertisementParam.ID) @Positive id: Long,
            @RequestBody @Valid updateAdTitle: UpdateAdTitle,
            principal: Principal
        ): ResponseEntity<String> {
            advertisementCommandService.editAdTitle(id, updateAdTitle, UUID.fromString(principal.name))
            logger().info(AdControllerLog.EDIT_TITLE_SUCCESS + id)

            return AdvertisementResponse.editTitleSuccess()
        }

        @PatchMapping(AdvertisementUrl.EDIT_CONTENT)
        fun editAdContent(
            @PathVariable(AdvertisementParam.ID) @Positive id: Long,
            @RequestBody @Valid updateAdContent: UpdateAdContent,
            principal: Principal
        ): ResponseEntity<String> {
            advertisementCommandService.editAdContent(id, updateAdContent, UUID.fromString(principal.name))
            logger().info(AdControllerLog.EDIT_CONTENT_SUCCESS + id)

            return AdvertisementResponse.editContentSuccess()
        }

        @DeleteMapping(AdvertisementUrl.REMOVE_AD)
        fun removeAd(
            @PathVariable(AdvertisementParam.ID) @Positive id: Long,
            principal: Principal
        ): ResponseEntity<String> {
            advertisementCommandService.removeAd(id, UUID.fromString(principal.name))
            logger().info(AdControllerLog.REMOVE_SUCCESS + id)

            return AdvertisementResponse.removeAdSuccess()
        }
    }
