package howru.howru.subscribe.exception

enum class SubscribeExceptionMessage(val status: Int, val message: String) {
    SUBSCRIBE_IS_NULL(404, "구독정보가 존재하지 않습니다. 구독자 ID : "),
    NOT_FOLLOWER(400, "팔로워가 아니라 잠금 회원의 게시글을 볼 수 없습니다. 구독자 ID : "),
    IS_NOT_FOLLOW_EACH(400, "맞팔로우하지 않은 회원의 댓글을 확인하는 것은 불가능합니다. 구독자 ID : ")
}
