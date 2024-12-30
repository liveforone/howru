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

object SubscribeApiDocs {
    const val TAG_NAME = "Subscribe"
    const val FOLLOWING_INFO_SUMMARY = "회원의 팔로잉 페이징 조회"
    const val FOLLOWING_INFO_DESCRIPTION = "Cursor based 페이징울 사용합니다."
    const val FOLLOWER_INFO_SUMMARY = "회원의 팔로워 페이징 조회"
    const val FOLLOWER_INFO_DESCRIPTION = "Cursor based 페이징울 사용합니다."
    const val COUNT_FOLLOWING_SUMMARY = "팔로잉 갯수 조회"
    const val COUNT_FOLLOWER_SUMMARY = "팔로워 갯수 조회"
    const val SUBSCRIBE_SUMMARY = "구독 신청"
    const val UNSUBSCRIBE_SUMMARY = "구독 취소 신청"
}
