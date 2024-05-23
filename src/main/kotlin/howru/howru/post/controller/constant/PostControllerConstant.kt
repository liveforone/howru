package howru.howru.post.controller.constant

object PostUrl {
    const val DETAIL = "/posts/{id}"
    const val ALL = "/posts"
    const val MY_POST = "/posts/{memberId}/my"
    const val OTHER_MEMBER_POST = "/posts/{memberId}/other"
    const val POST_OF_FOLLOWEE = "/posts/{memberId}/followee"
    const val RECOMMEND = "/posts/recommend"
    const val RANDOM = "/posts/random"
    const val COUNT_MEMBER_POST = "/posts/{memberId}/count"
    const val CREATE = "/posts"
    const val EDIT = "/posts/{id}"
    const val REMOVE = "/posts/{id}"
}

object PostParam {
    const val ID = "id"
    const val MEMBER_ID = "memberId"
    const val LAST_ID = "lastId"
    const val CONTENT = "content"
}
