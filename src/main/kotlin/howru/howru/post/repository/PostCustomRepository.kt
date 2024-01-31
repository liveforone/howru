package howru.howru.post.repository

import howru.howru.post.domain.Post
import howru.howru.post.dto.response.PostInfo
import java.util.UUID

interface PostCustomRepository {
    fun findOneById(id: Long): Post
    fun findOneByIdAndWriter(id: Long, writerId: UUID): Post
    fun findOneDtoById(id: Long): PostInfo
    fun findMyPosts(memberId: UUID, lastId: Long?): List<PostInfo>
    fun findAllPosts(lastId: Long?): List<PostInfo>
    fun findPostsBySomeone(someoneId: UUID, lastId: Long?): List<PostInfo>
    fun findPostsByFollowee(followeeId: List<UUID>, lastId: Long?): List<PostInfo>
    fun findRecommendPosts(keyword: String?): List<PostInfo>
    fun countOfPostByWriter(writerId: UUID): Long
}