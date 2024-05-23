package howru.howru.advertisement.controller.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

private object CommandSuccessMessage {
    const val CREATE_HALF_AD_SUCCESS = "6개월 광고 등록를 성공적으로 등록하였습니다."
    const val CREATE_YEAR_AD_SUCCESS = "1년 광고 등록를 성공적으로 등록하였습니다."
    const val EDIT_AD_SUCCESS = "광고 제목과 내용을 성공적으로 수정하였습니다."
    const val REMOVE_AD_SUCCESS = "광고를 성공적으로 삭제하였습니다."
}

object AdvertisementResponse {
    fun createHalfAdSuccess() =
        ResponseEntity
            .status(HttpStatus.CREATED)
            .body(CommandSuccessMessage.CREATE_HALF_AD_SUCCESS)

    fun createYearAdSuccess() =
        ResponseEntity
            .status(HttpStatus.CREATED)
            .body(CommandSuccessMessage.CREATE_YEAR_AD_SUCCESS)

    fun editAdSuccess() = ResponseEntity.ok(CommandSuccessMessage.EDIT_AD_SUCCESS)

    fun removeAdSuccess() = ResponseEntity.ok(CommandSuccessMessage.REMOVE_AD_SUCCESS)
}
