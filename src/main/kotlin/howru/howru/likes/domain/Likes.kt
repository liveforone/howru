package howru.howru.likes.domain

import howru.howru.globalUtil.getCurrentTimestamp
import howru.howru.likes.domain.constant.LikesConstant
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import java.util.UUID

@Entity
@IdClass(LikesPk::class)
class Likes private constructor(
    @Id @Column(name = LikesConstant.MEMBER_UUID) val memberUUID: UUID,
    @Id @Column(name = LikesConstant.POST_ID) val postId: Long,
    @Column(updatable = false) val timestamp: Int = getCurrentTimestamp()
) {
    companion object {
        fun create(memberUUID: UUID, postId: Long) = Likes(memberUUID, postId)
    }
}