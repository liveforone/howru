package howru.howru.advertisement.controller

import howru.howru.advertisement.controller.constant.AdvertisementControllerLog
import howru.howru.advertisement.controller.constant.AdvertisementParam
import howru.howru.advertisement.controller.constant.AdvertisementUrl
import howru.howru.advertisement.controller.response.AdvertisementResponse
import howru.howru.advertisement.dto.request.CreateAdvertisement
import howru.howru.advertisement.dto.update.UpdateAdContent
import howru.howru.advertisement.dto.update.UpdateAdTitle
import howru.howru.advertisement.service.command.AdvertisementCommandService
import howru.howru.advertisement.service.query.AdvertisementQueryService
import howru.howru.globalUtil.validateBinding
import howru.howru.logger
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import java.util.UUID

@RestController
class AdvertisementController @Autowired constructor(
    private val advertisementQueryService: AdvertisementQueryService,
    private val advertisementCommandService: AdvertisementCommandService
) {
    @GetMapping(AdvertisementUrl.DETAIL)
    fun detail(@PathVariable(AdvertisementParam.ID) id: Long): ResponseEntity<*> {
        val ad = advertisementQueryService.getOneById(id)
        return AdvertisementResponse.detailSuccess(ad)
    }

    @GetMapping(AdvertisementUrl.ALL_AD)
    fun allAd(): ResponseEntity<*> {
        val ads = advertisementQueryService.getAllAdvertisement()
        return AdvertisementResponse.allAdSuccess(ads)
    }

    @GetMapping(AdvertisementUrl.SEARCH_COMPANY)
    fun searchAdByCompany(@RequestParam(AdvertisementParam.COMPANY) company: String): ResponseEntity<*> {
        val ads = advertisementQueryService.searchAdByCompany(company)
        return AdvertisementResponse.searchAdByCompanySuccess(ads)
    }

    @GetMapping(AdvertisementUrl.EXPIRED_AD)
    fun expiredAd(): ResponseEntity<*> {
        val ads = advertisementQueryService.getExpiredAd()
        return AdvertisementResponse.expiredAdSuccess(ads)
    }

    @GetMapping(AdvertisementUrl.RANDOM)
    fun randomAd(): ResponseEntity<*> {
        val ad = advertisementQueryService.getRandomAd()
        return AdvertisementResponse.randomAdSuccess(ad)
    }

    @PostMapping(AdvertisementUrl.CREATE_HALF)
    fun createHalf(
        @RequestBody @Valid createAdvertisement: CreateAdvertisement,
        bindingResult: BindingResult,
        principal: Principal
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        advertisementCommandService.createHalfAd(createAdvertisement, UUID.fromString(principal.name))
        logger().info(AdvertisementControllerLog.CREATE_SUCCESS.log)

        return AdvertisementResponse.createHalfSuccess()
    }

    @PostMapping(AdvertisementUrl.CREATE_YEAR)
    fun createYear(
        @RequestBody @Valid createAdvertisement: CreateAdvertisement,
        bindingResult: BindingResult,
        principal: Principal
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        advertisementCommandService.createYearAd(createAdvertisement, UUID.fromString(principal.name))
        logger().info(AdvertisementControllerLog.CREATE_SUCCESS.log)

        return AdvertisementResponse.createYearSuccess()
    }

    @PutMapping(AdvertisementUrl.EDIT_TITLE)
    fun editTitle(
        @RequestBody @Valid updateAdTitle: UpdateAdTitle,
        bindingResult: BindingResult,
        principal: Principal
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        advertisementCommandService.editTitle(updateAdTitle, UUID.fromString(principal.name))
        logger().info(AdvertisementControllerLog.EDIT_TITLE_SUCCESS.log)

        return AdvertisementResponse.editTitleSuccess()
    }

    @PutMapping(AdvertisementUrl.EDIT_CONTENT)
    fun editContent(
        @RequestBody @Valid updateAdContent: UpdateAdContent,
        bindingResult: BindingResult,
        principal: Principal
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        advertisementCommandService.editContent(updateAdContent, UUID.fromString(principal.name))
        logger().info(AdvertisementControllerLog.EDIT_CONTENT_SUCCESS.log)

        return AdvertisementResponse.editContentSuccess()
    }

    @DeleteMapping(AdvertisementUrl.DELETE)
    fun deleteAd(
        @PathVariable(AdvertisementParam.ID) id: Long,
        principal: Principal
    ): ResponseEntity<*> {
        advertisementCommandService.deleteAdByUUID(id, UUID.fromString(principal.name))
        logger().info(AdvertisementControllerLog.DELETE_SUCCESS.log)

        return AdvertisementResponse.deleteAdSuccess()
    }
}