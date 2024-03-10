package howru.howru.likes.repository

import howru.howru.exception.exception.LikesException
import howru.howru.exception.message.LikesExceptionMessage
import howru.howru.likes.domain.Likes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class LikesQuery @Autowired constructor(
    private val likesRepository: LikesRepository
) {
    fun findOneById(memberId: UUID, postId: Long): Likes {
        return likesRepository.findAll {
            select(entity(Likes::class))
                .from(entity(Likes::class))
                .where(path(Likes::memberId).eq(memberId).and(path(Likes::postId).eq(postId)))
        }.firstOrNull() ?: throw LikesException(LikesExceptionMessage.LIKES_IS_NULL, postId)
    }

    fun countOfLikesByPost(postId: Long) = likesRepository.countOfLikesByPost(postId)
}