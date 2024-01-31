package howru.howru.comments.dto.response

import howru.howru.comments.domain.CommentsState
import java.util.UUID

data class CommentsInfo(val id: Long, val writerId: UUID, val postId: Long, val content: String, val commentsState: CommentsState, val createdDatetime: Long)