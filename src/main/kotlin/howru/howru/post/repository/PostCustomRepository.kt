package howru.howru.post.repository

import howru.howru.post.domain.Post
import howru.howru.post.dto.response.PostInfo
import java.util.UUID

interface PostCustomRepository {
    fun findOneById(id: Long): Post
    fun findOneByIdAndWriter(id: Long, writerUUID: UUID): Post
    fun findOneDtoById(id: Long): PostInfo
    fun findMyPosts(memberUUID: UUID, lastId: Long?): List<PostInfo>
    fun findAllPosts(lastId: Long?): List<PostInfo>
    fun findPostsBySomeone(someoneUUID: UUID, lastId: Long?): List<PostInfo>
    fun findPostsByFollowee(followeeUUID: List<UUID>, lastId: Long?): List<PostInfo>
    fun findRecommendPosts(keyword: String?): List<PostInfo>
    fun countPostByWriter(writerUUID: UUID): Long
}