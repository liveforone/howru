package howru.howru.post.repository

import howru.howru.post.domain.Post
import howru.howru.post.dto.response.PostInfo
import java.util.UUID

interface PostCustomRepository {
    fun findOneByUUID(uuid: UUID): Post
    fun findOneByUUIDAndWriter(uuid: UUID, writerUUID: UUID): Post
    fun findOneDtoByUUID(uuid: UUID): PostInfo
    fun findMyPosts(memberUUID: UUID, lastUUID: UUID?): List<PostInfo>
    fun findAllPosts(lastUUID: UUID?): List<PostInfo>
    fun findPostsBySomeone(someoneUUID: UUID, lastUUID: UUID?): List<PostInfo>
    fun findPostsByFollowee(followeeUUID: List<UUID>, lastUUID: UUID?): List<PostInfo>
    fun findRecommendPosts(keyword: String?): List<PostInfo>
    fun countPostByWriter(writerUUID: UUID): Long
}