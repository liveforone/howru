package howru.howru.post.repository

import howru.howru.post.domain.Post
import howru.howru.post.dto.response.PostInfo
import howru.howru.post.dto.response.PostPage
import java.util.*

interface PostCustomRepository {
    fun findPostById(id: Long): Post

    fun findPostByIdAndWriter(
        id: Long,
        writerId: UUID
    ): Post

    fun findPostInfoById(id: Long): PostInfo

    fun findPostsByMember(
        memberId: UUID,
        lastId: Long?
    ): PostPage

    fun findAllPosts(lastId: Long?): PostPage

    fun findPostsByFolloweeIds(
        followeeId: List<UUID>,
        lastId: Long?
    ): PostPage

    fun findRecommendPosts(
        keyword: String?,
        lastId: Long?
    ): PostPage
}
