package howru.howru.subscribe.controller.constant

object
SubscribeUrl {
    const val GET_FOLLOWING = "/subscribe/{followerUUID}/following"
    const val GET_FOLLOWER = "/subscribe/{followeeUUID}/follower"
    const val COUNT_FOLLOWING = "/subscribe/{followerUUID}/count/following"
    const val COUNT_FOLLOWER = "/subscribe/{followeeUUID}/count/follower"
    const val SUBSCRIBE = "/subscribe/create"
    const val UNSUBSCRIBE = "/unsubscribe"
}