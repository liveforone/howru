package howru.howru.advertisement.controller.response

import howru.howru.advertisement.dto.response.AdvertisementInfo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object AdvertisementResponse {
    private const val CREATE_HALF_SUCCESS = "6개월 광고 등록를 성공적으로 등록하였습니다."
    private const val CREATE_YEAR_SUCCESS = "1년 광고 등록를 성공적으로 등록하였습니다."
    private const val EDIT_TITLE_SUCCESS = "광고 제목을 성공적으로 수정하였습니다."
    private const val EDIT_CONTENT_SUCCESS = "광고 내용을 성공적으로 수정하였습니다."
    private const val REMOVE_SUCCESS = "광고를 성공적으로 삭제하였습니다."

    fun detailSuccess(ad: AdvertisementInfo) = ResponseEntity.ok(ad)
    fun allAdSuccess(ads: List<AdvertisementInfo>) = ResponseEntity.ok(ads)
    fun searchAdByCompanySuccess(ads: List<AdvertisementInfo>) = ResponseEntity.ok(ads)
    fun expiredAdSuccess(ads: List<AdvertisementInfo>) = ResponseEntity.ok(ads)
    fun randomAdSuccess(ad: AdvertisementInfo) = ResponseEntity.ok(ad)
    fun createHalfSuccess() = ResponseEntity.status(HttpStatus.CREATED).body(CREATE_HALF_SUCCESS)
    fun createYearSuccess() = ResponseEntity.status(HttpStatus.CREATED).body(CREATE_YEAR_SUCCESS)
    fun editTitleSuccess() = ResponseEntity.ok(EDIT_TITLE_SUCCESS)
    fun editContentSuccess() = ResponseEntity.ok(EDIT_CONTENT_SUCCESS)
    fun removeAdSuccess() = ResponseEntity.ok(REMOVE_SUCCESS)
}