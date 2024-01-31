package howru.howru.post.controller.constant

object PostUrl {
    const val DETAIL = "/post/{id}"
    const val MY_POST = "/post/my-post/{memberId}"
    const val ALL_POST = "/post"
    const val POST_OF_WRITER = "/post/writer/{writerId}"
    const val POST_OF_FOLLOWEE = "/post/followee/{followerId}"
    const val RECOMMEND = "/post/recommend"
    const val RANDOM = "/post/random"
    const val COUNT_POST_BY_WRITER = "/post/count/{writerId}"
    const val CREATE = "/post/create"
    const val EDIT_CONTENT = "/post/{id}/edit-content"
    const val REMOVE = "/post/{id}/remove"
}