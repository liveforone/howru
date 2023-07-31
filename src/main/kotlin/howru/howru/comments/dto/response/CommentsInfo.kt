package howru.howru.comments.dto.response

import howru.howru.comments.domain.CommentsState
import java.util.UUID

data class CommentsInfo(val uuid: UUID, val writerUUID: UUID, val postUUID: UUID, val content: String, val commentsState: CommentsState, val createdDatetime: Long)