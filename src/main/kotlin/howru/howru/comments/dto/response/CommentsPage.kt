package howru.howru.comments.dto.response

data class CommentsPage(
    val commentsInfoList: List<CommentsInfo>,
    val lastId: Long
)
