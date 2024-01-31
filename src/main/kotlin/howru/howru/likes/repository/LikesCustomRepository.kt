package howru.howru.likes.repository

import howru.howru.likes.domain.Likes
import howru.howru.likes.dto.response.LikesBelongMemberInfo
import howru.howru.likes.dto.response.LikesBelongPostInfo
import java.util.*

interface LikesCustomRepository {
    fun findOneById(memberId: UUID, postId: Long): Likes
    fun countOfLikesByPost(postId: Long): Long
    fun findLikesBelongMember(memberId: UUID, lastPostId: Long?): List<LikesBelongMemberInfo>
    fun findLikesBelongPost(postId: Long, lastMemberId: UUID?): List<LikesBelongPostInfo>
}