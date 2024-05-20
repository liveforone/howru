package howru.howru.post.repository

import howru.howru.post.domain.Post
import howru.howru.post.domain.vo.PostInfo
import howru.howru.post.domain.vo.PostPage
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
    ): PostPage

    fun findAllPosts(lastId: Long?): PostPage

    fun findPostsBySomeone(
        someoneId: UUID,
        lastId: Long?
    ): PostPage

    fun findPostsByFollowee(
        followeeId: List<UUID>,
        lastId: Long?
    ): PostPage

    fun findRecommendPosts(
        keyword: String?,
        lastId: Long?
    ): PostPage
}
