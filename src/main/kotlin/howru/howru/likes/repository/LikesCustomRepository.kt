package howru.howru.likes.repository

import howru.howru.likes.domain.Likes
import howru.howru.likes.dto.response.LikesBelongMemberInfo
import howru.howru.likes.dto.response.LikesBelongPostInfo
import java.util.*

interface LikesCustomRepository {
    fun findOneByUUID(memberUUID: UUID, postUUID: UUID): Likes
    fun findLikesBelongMember(memberUUID: UUID): List<LikesBelongMemberInfo>
    fun findLikesBelongPost(postUUID: UUID): List<LikesBelongPostInfo>
}