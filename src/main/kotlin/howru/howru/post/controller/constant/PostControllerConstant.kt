package howru.howru.post.controller.constant

object PostUrl {
    const val DETAIL = "/posts/{id}"
    const val ALL = "/posts"
    const val MY_POST = "/posts/my"
    const val POST_OF_FOLLOWEE = "/posts/followees"
    const val RECOMMEND = "/posts/recommend"
    const val RANDOM = "/posts/random"
    const val CREATE = "/posts"
    const val EDIT = "/posts/{id}"
    const val REMOVE = "/posts/{id}"
    const val COMMENTS_PAGE = "/posts/{id}/comments"
}

object PostParam {
    const val ID = "id"
    const val LAST_ID = "lastId"
    const val CONTENT = "content"
}
