package howru.howru.likes.repository

import howru.howru.likes.domain.Likes
import howru.howru.likes.dto.response.LikesBelongMemberInfo
import howru.howru.likes.dto.response.LikesBelongPostInfo
import java.util.*

interface LikesCustomRepository {
    fun findOneById(memberUUID: UUID, postId: Long): Likes
    fun findLikesBelongMember(memberUUID: UUID, lastPostId: Long?): List<LikesBelongMemberInfo>
    fun findLikesBelongPost(postId: Long, lastMemberUUID: UUID?): List<LikesBelongPostInfo>
}