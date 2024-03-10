package howru.howru.likes.service.query

import howru.howru.likes.repository.LikesQuery
import howru.howru.likes.repository.LikesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class LikesQueryService @Autowired constructor(
    private val likesQuery: LikesQuery
) {
    fun getCountOfLikesByPost(postId: Long) = likesQuery.countOfLikesByPost(postId)
}