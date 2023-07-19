package howru.howru.subscribe.controller.constant

object SubscribeUrl {
    const val GET_FOLLOWING = "/subscribe/following/{followerUUID}"
    const val GET_FOLLOWER = "/subscribe/follower/{followeeUUID}"
    const val COUNT_FOLLOWING = "/subscribe/count/following/{followerUUID}"
    const val COUNT_FOLLOWER = "/subscribe/count/follower/{followeeUUID}"
    const val SUBSCRIBE = "/subscribe/create"
    const val UNSUBSCRIBE = "/unsubscribe"
}