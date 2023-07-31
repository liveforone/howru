package howru.howru.exception.message

enum class AdvertisementExceptionMessage(val status: Int, val message: String) {
    AD_IS_NULL(404, "광고가 존재하지 않습니다."),
    NOT_ADMIN(401, "운영자가 아니여서 광고 접근이 불가능합니다.")
}