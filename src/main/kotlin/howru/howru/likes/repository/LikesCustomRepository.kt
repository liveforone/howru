package howru.howru.likes.repository

import howru.howru.likes.domain.Likes
import howru.howru.likes.dto.response.LikesBelongMemberInfo
import howru.howru.likes.dto.response.LikesBelongPostInfo
import java.util.*

interface LikesCustomRepository {
    fun findLikesById(memberId: UUID, postId: Long): Likes
    fun findLikesBelongMember(memberId: UUID, lastTimestamp: Int?): List<LikesBelongMemberInfo>
    fun findLikesBelongPost(postId: Long, lastTimestamp: Int?): List<LikesBelongPostInfo>
}