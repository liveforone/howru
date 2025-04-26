package howru.howru.likes.repository

import howru.howru.likes.domain.Likes
import howru.howru.likes.domain.LikesPk
import howru.howru.likes.repository.sql.LikesSql
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface LikesRepository :
    JpaRepository<Likes, LikesPk>,
    LikesCustomRepository {
    @Query(LikesSql.COUNT_OF_LIKES_BY_POST_QUERY)
    fun countOfLikesByPost(
        @Param(LikesSql.POST_ID) postId: Long
    ): Long
}
