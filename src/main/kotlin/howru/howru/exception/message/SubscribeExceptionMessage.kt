package howru.howru.exception.message

enum class SubscribeExceptionMessage(val status: Int, val message: String) {
    SUBSCRIBE_IS_NULL(404, "구독정보가 존재하지 않습니다.")
}