package howru.howru.post.controller.constant

object PostUrl {
    const val DETAIL = "/post/detail/{id}"
    const val MY_POST = "/post/my-post/{memberUUID}"
    const val ALL_POST = "/post/all"
    const val POST_OF_WRITER = "/post/writer/{writerUUID}"
    const val POST_OF_FOLLOWEE = "/post/followee/{followerUUID}"
    const val RECOMMEND = "/post/recommend"
    const val RANDOM = "/post/random"
    const val COUNT_POST_BY_WRITER = "/post/count/{writerUUID}"
    const val CREATE = "/post/create"
    const val EDIT_CONTENT = "/post/edit/content"
    const val REMOVE = "/post/remove"
}