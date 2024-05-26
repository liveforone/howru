package howru.howru.comments.service.integrated

import howru.howru.reply.service.query.ReplyQueryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class IntegratedCommentsService
    @Autowired
    constructor(
        private val replyQueryService: ReplyQueryService
    ) {
        fun getRepliesByComment(
            commentId: Long,
            lastId: Long?
        ) = replyQueryService.getRepliesByComment(commentId, lastId)
    }
