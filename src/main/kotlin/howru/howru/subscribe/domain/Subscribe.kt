package howru.howru.subscribe.domain

import howru.howru.globalUtil.getCurrentTimestamp
import howru.howru.subscribe.domain.constant.SubscribeConstant
import jakarta.persistence.*
import java.util.*

@Entity
@IdClass(SubscribePk::class)
class Subscribe private constructor(
    @Id @Column(name = SubscribeConstant.FOLLOWEE_UUID) val followeeUUID: UUID,
    @Id @Column(name = SubscribeConstant.FOLLOWER_UUID) val followerUUID: UUID,
    @Column(updatable = false) val timestamp: Int = getCurrentTimestamp()
) {
    companion object {
        fun create(followeeUUID: UUID, followerUUID: UUID) = Subscribe(followeeUUID, followerUUID)
    }
}