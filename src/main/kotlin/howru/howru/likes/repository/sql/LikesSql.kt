package howru.howru.likes.repository.sql

object LikesSql {
    const val COUNT_OF_LIKES_BY_POST_QUERY = "select count(*) from Likes l where l.postId = :postId"
    const val POST_ID = "postId"
}