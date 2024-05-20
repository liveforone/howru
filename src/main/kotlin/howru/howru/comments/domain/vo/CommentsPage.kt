package howru.howru.comments.domain.vo

data class CommentsPage(
    val commentsInfoList: List<CommentsInfo>,
    val lastId: Long
)
