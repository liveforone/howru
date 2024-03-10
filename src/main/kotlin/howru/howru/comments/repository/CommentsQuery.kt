package howru.howru.comments.repository

import howru.howru.comments.domain.Comments
import howru.howru.comments.dto.response.CommentsInfo
import howru.howru.comments.repository.constant.CommentsRepoConstant
import howru.howru.exception.exception.CommentsException
import howru.howru.exception.message.CommentsExceptionMessage
import howru.howru.member.domain.Member
import howru.howru.post.domain.Post
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.util.*

@Component
class CommentsQuery @Autowired constructor(
    private val commentsRepository: CommentsRepository
) {

    fun findOneById(id: Long): Comments {
        return commentsRepository.findAll {
            select(entity(Comments::class))
                .from(entity(Comments::class))
                .where(path(Comments::id).eq(id))
        }.firstOrNull() ?: throw CommentsException(CommentsExceptionMessage.COMMENTS_IS_NULL, id)
    }

    fun findOneByIdAndWriter(id: Long, writerId: UUID): Comments {
        return commentsRepository.findAll {
            select(entity(Comments::class))
                .from(entity(Comments::class), join(Comments::writer))
                .where(path(Comments::id).eq(id).and(path(Member::id).eq(writerId)))
        }.firstOrNull() ?: throw CommentsException(CommentsExceptionMessage.COMMENTS_IS_NULL, id)
    }

    fun findOneDtoById(id: Long): CommentsInfo {
        return commentsRepository.findAll {
            selectNew<CommentsInfo>(
                path(Comments::id),
                path(Member::id),
                path(Post::id),
                path(Comments::content),
                path(Comments::commentsState),
                path(Comments::createdDatetime),
            ).from(entity(Comments::class), join(Comments::writer), join(Comments::post))
                .where(path(Comments::id).eq(id))
        }.firstOrNull() ?: throw CommentsException(CommentsExceptionMessage.COMMENTS_IS_NULL, id)
    }

    fun findCommentsByWriter(writerId: UUID, page: Int): List<CommentsInfo> {
        val pageable =  PageRequest.of(page, CommentsRepoConstant.LIMIT_PAGE)
        return commentsRepository.findAll(pageable) {
            selectNew<CommentsInfo>(
                path(Comments::id),
                path(Member::id),
                path(Post::id),
                path(Comments::content),
                path(Comments::commentsState),
                path(Comments::createdDatetime),
            ).from(entity(Comments::class), join(Comments::writer), join(Comments::post))
                .where(path(Member::id).eq(writerId))
                .orderBy(path(Comments::id).desc())
        }.filterNotNull()
    }

    fun findCommentsByPost(postId: Long, page: Int): List<CommentsInfo> {
        val pageable =  PageRequest.of(page, CommentsRepoConstant.LIMIT_PAGE)
        return commentsRepository.findAll(pageable) {
            selectNew<CommentsInfo>(
                path(Comments::id),
                path(Member::id),
                path(Post::id),
                path(Comments::content),
                path(Comments::commentsState),
                path(Comments::createdDatetime),
            ).from(entity(Comments::class), join(Comments::writer), join(Comments::post))
                .where(path(Post::id).eq(postId))
                .orderBy(path(Comments::id).desc())
        }.filterNotNull()
    }
}