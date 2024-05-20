package howru.howru.advertisement.exception

enum class AdvertisementExceptionMessage(val status: Int, val message: String) {
    AD_IS_NULL(404, "광고가 존재하지 않습니다. 광고 ID : ")
}
