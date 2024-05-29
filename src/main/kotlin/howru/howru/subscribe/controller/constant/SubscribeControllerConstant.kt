package howru.howru.subscribe.controller.constant

object SubscribeUrl {
    const val FOLLOWING_INFO = "/subscribes/{memberId}/following"
    const val FOLLOWER_INFO = "/subscribes/{memberId}/follower"
    const val COUNT_FOLLOWING = "/subscribes/{memberId}/count/following"
    const val COUNT_FOLLOWER = "/subscribes/{memberId}/count/follower"
    const val SUBSCRIBE = "/subscribes"
    const val UNSUBSCRIBE = "/subscribes/unsubscribe"
}

object SubscribeParam {
    const val MEMBER_ID = "memberId"
    const val LAST_TIMESTAMP = "last-timestamp"
}
