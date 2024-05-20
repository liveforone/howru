package howru.howru.subscribe.domain

import howru.howru.global.util.getCurrentTimestamp
import howru.howru.subscribe.domain.constant.SubscribeConstant
import jakarta.persistence.*
import java.util.*

@Entity
@IdClass(SubscribePk::class)
class Subscribe private constructor(
    @Id @Column(name = SubscribeConstant.FOLLOWEE_ID) val followeeId: UUID,
    @Id @Column(name = SubscribeConstant.FOLLOWER_ID) val followerId: UUID,
    @Column(updatable = false) val timestamp: Int = getCurrentTimestamp()
) {
    companion object {
        fun create(
            followeeId: UUID,
            followerId: UUID
        ) = Subscribe(followeeId, followerId)
    }
}
