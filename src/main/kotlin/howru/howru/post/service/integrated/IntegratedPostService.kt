package howru.howru.post.service.integrated

import howru.howru.comments.service.query.CommentsQueryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class IntegratedPostService
    @Autowired
    constructor(
        private val commentsQueryService: CommentsQueryService
    ) {
        fun getCommentsByPost(
            postId: Long,
            lastId: Long?
        ) = commentsQueryService.getCommentsByPost(postId, lastId)
    }
