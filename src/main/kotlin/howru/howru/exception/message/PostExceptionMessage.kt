package howru.howru.exception.message

enum class PostExceptionMessage(val status:Int, val message: String) {
    POST_IS_NULL(404, "게시글이 존재하지 않습니다."),
    NOT_FOLLOWER(400, "팔로워가 아니라 잠금 회원의 게시글을 볼 수 없습니다.")
}