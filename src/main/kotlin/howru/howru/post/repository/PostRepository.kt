package howru.howru.post.repository

import howru.howru.post.domain.Post
import howru.howru.post.dto.response.PostInfo
import howru.howru.post.repository.sql.PostSql
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PostRepository : JpaRepository<Post, Long>, PostCustomRepository {
    @Query(PostSql.RANDOM_POSTS_QUERY)
    fun findRandomPosts(): List<PostInfo>
}