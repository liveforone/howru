package howru.howru.likes.service.query

import howru.howru.likes.repository.LikesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class LikesQueryService @Autowired constructor(
    private val likesRepository: LikesRepository
) {
    fun getLikesBelongMember(memberUUID: UUID) = likesRepository.findLikesBelongMember(memberUUID)
    fun getLikesBelongPost(postUUID: UUID) = likesRepository.findLikesBelongPost(postUUID)
}