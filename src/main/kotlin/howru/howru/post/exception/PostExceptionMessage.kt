package howru.howru.post.exception

enum class PostExceptionMessage(
    val status: Int,
    val message: String
) {
    POST_IS_NULL(404, "게시글이 존재하지 않습니다. 게시글 ID : ")
}
