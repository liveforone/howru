package howru.howru.post.repository

import howru.howru.post.domain.Post
import howru.howru.post.dto.response.PostInfo
import java.util.*

interface PostCustomRepository {
    fun findPostById(id: Long): Post

    fun findPostByIdAndWriter(
        id: Long,
        writerId: UUID
    ): Post

    fun findPostInfoById(id: Long): PostInfo

    fun findPostsByWriter(
        memberId: UUID,
        lastId: Long?
    ): List<PostInfo>

    fun findAllPosts(lastId: Long?): List<PostInfo>

    fun findPostsBySomeone(
        someoneId: UUID,
        lastId: Long?
    ): List<PostInfo>

    fun findPostsByFollowee(
        followeeId: List<UUID>,
        lastId: Long?
    ): List<PostInfo>

    fun findRecommendPosts(
        keyword: String?,
        lastId: Long?
    ): List<PostInfo>
}
