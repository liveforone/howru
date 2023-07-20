package howru.howru.post.dto.response

import java.util.UUID

class PostInfo(
    val uuid: UUID,
    val writerUUID: UUID,
    val content: String,
    val createdDate: Long
)
