package howru.howru.comments.service.query

import howru.howru.comments.repository.CommentsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class CommentsQueryService
    @Autowired
    constructor(
        private val commentsRepository: CommentsRepository
    ) {
        fun getCommentById(id: Long) = commentsRepository.findCommentsInfoById(id)

        fun getCommentsByMember(
            memberId: UUID,
            lastId: Long?
        ) = commentsRepository.findCommentsByMember(memberId, lastId)

        fun getCommentsByPost(
            postId: Long,
            lastId: Long?
        ) = commentsRepository.findCommentsByPost(postId, lastId)
    }
