package howru.howru.subscribe.domain

import howru.howru.globalUtil.getCurrentTimestamp
import jakarta.persistence.*
import java.util.*

@Entity
@IdClass(SubscribePk::class)
class Subscribe private constructor(
    @Id @Column(name = "followee_uuid") val followeeUUID: UUID,
    @Id @Column(name = "follower_uuid") val followerUUID: UUID,
    @Column(updatable = false) val timestamp: Int = getCurrentTimestamp()
) {
    companion object {
        fun create(followeeUUID: UUID, followerUUID: UUID) = Subscribe(followeeUUID, followerUUID)
    }
}