package howru.howru.post.dto.response

data class PostPage(
    val postInfoList: List<PostInfo>,
    val lastId: Long
)
