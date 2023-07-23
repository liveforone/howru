package howru.howru.exception.message

enum class LikesExceptionMessage(val status: Int, val message: String) {
    LIKES_IS_NULL(404, "좋아요가 존재하지 않습니다.")
}