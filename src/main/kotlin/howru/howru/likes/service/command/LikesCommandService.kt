package howru.howru.likes.service.command

import howru.howru.likes.domain.Likes
import howru.howru.likes.dto.request.CreateLikes
import howru.howru.likes.dto.request.RemoveLikes
import howru.howru.likes.repository.LikesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class LikesCommandService @Autowired constructor(
    private val likesRepository: LikesRepository
) {
    fun createLikes(createLikes: CreateLikes) {
        with(createLikes) {
            Likes.create(memberId!!, postId!!).also { likesRepository.save(it) }
        }
    }

    fun removeLikes(removeLikes: RemoveLikes) {
        with(removeLikes) {
            likesRepository.findLikesById(memberId!!, postId!!).also { likesRepository.delete(it) }
        }
    }
}