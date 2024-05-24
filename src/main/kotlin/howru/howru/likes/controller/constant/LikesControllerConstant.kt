package howru.howru.likes.controller.constant

object LikesUrl {
    const val COUNT_OF_LIKES = "/likes/{postId}/count"
    const val LIKES_PAGE = "/likes"
    const val LIKE = "/likes"
    const val DISLIKE = "/likes/dislike"
}

object LikesParam {
    const val MEMBER_ID = "memberId"
    const val POST_ID = "postId"
    const val LAST_TIMESTAMP = "lastTimestamp"
}
