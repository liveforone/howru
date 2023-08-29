package howru.howru.post.dto.response

import howru.howru.post.domain.PostState
import java.util.UUID

class PostInfo(val id: Long, val writerUUID: UUID, val content: String, val postState: PostState, val createdDatetime: Long)