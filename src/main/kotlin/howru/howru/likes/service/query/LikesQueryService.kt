package howru.howru.likes.service.query

import howru.howru.likes.repository.LikesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class LikesQueryService
    @Autowired
    constructor(
        private val likesRepository: LikesRepository
    ) {
        fun getCountOfLikesByPost(postId: Long) = likesRepository.countOfLikesByPost(postId)

        fun getLikesBelongMember(
            memberId: UUID,
            lastTimestamp: Int?
        ) = likesRepository.findLikesBelongMember(memberId, lastTimestamp)

        fun getLikesBelongPost(
            postId: Long,
            lastTimestamp: Int?
        ) = likesRepository.findLikesBelongPost(postId, lastTimestamp)
    }
