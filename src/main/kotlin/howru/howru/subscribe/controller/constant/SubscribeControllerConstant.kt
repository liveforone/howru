package howru.howru.subscribe.controller.constant

object SubscribeUrl {
    const val GET_FOLLOWING = "/subscribe/{followerId}/following"
    const val GET_FOLLOWER = "/subscribe/{followeeId}/follower"
    const val COUNT_FOLLOWING = "/subscribe/{followerId}/count/following"
    const val COUNT_FOLLOWER = "/subscribe/{followeeId}/count/follower"
    const val SUBSCRIBE = "/subscribe/create"
    const val UNSUBSCRIBE = "/unsubscribe"
}

object SubscribeParam {
    const val FOLLOWEE_ID = "followeeId"
    const val FOLLOWER_ID = "followerId"
    const val LAST_TIMESTAMP = "lastTimestamp"
}
