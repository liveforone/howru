package howru.howru.advertisement.controller

import howru.howru.advertisement.controller.constant.AdvertisementParam
import howru.howru.advertisement.controller.constant.AdvertisementUrl
import howru.howru.advertisement.controller.response.AdvertisementResponse
import howru.howru.advertisement.dto.request.CreateAdvertisement
import howru.howru.advertisement.dto.request.UpdateAdvertisement
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
        fun detail(
            @PathVariable(AdvertisementParam.ID) @Positive id: Long
        ): ResponseEntity<AdvertisementInfo> {
            val ad = advertisementQueryService.getOneById(id)
            return ResponseEntity.ok(ad)
        }

        @GetMapping(AdvertisementUrl.ALL_AD)
        fun allAd(): ResponseEntity<List<AdvertisementInfo?>> {
            val ads = advertisementQueryService.getAllAdvertisements()
            return ResponseEntity.ok(ads)
        }

        @GetMapping(AdvertisementUrl.SEARCH_COMPANY)
        fun searchCompany(
            @RequestParam(AdvertisementParam.COMPANY) company: String
        ): ResponseEntity<List<AdvertisementInfo?>> {
            val ads = advertisementQueryService.searchAdByCompany(company)
            return ResponseEntity.ok(ads)
        }

        @GetMapping(AdvertisementUrl.EXPIRED_AD)
        fun expiredAd(): ResponseEntity<List<AdvertisementInfo?>> {
            val ads = advertisementQueryService.getExpiredAds()
            return ResponseEntity.ok(ads)
        }

        @GetMapping(AdvertisementUrl.RANDOM_AD)
        fun randomAd(): ResponseEntity<AdvertisementInfo> {
            val ad = advertisementQueryService.getRandomAd()
            return ResponseEntity.ok(ad)
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

        @PatchMapping(AdvertisementUrl.EDIT_AD)
        fun editAd(
            @PathVariable(AdvertisementParam.ID) @Positive id: Long,
            @RequestBody @Valid updateAdvertisement: UpdateAdvertisement,
            principal: Principal
        ): ResponseEntity<String> {
            advertisementCommandService.editAd(id, updateAdvertisement, UUID.fromString(principal.name))
            logger().info(AdControllerLog.EDIT_SUCCESS + id)

            return AdvertisementResponse.editAdSuccess()
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
