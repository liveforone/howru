package howru.howru.post.repository

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import howru.howru.post.domain.Post
import howru.howru.post.dto.response.PostInfo
import howru.howru.post.repository.sql.PostSql
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface PostRepository : JpaRepository<Post, Long>, KotlinJdslJpqlExecutor {
    @Query(PostSql.RANDOM_POSTS_QUERY)
    fun findRandomPosts(): List<PostInfo>

    @Query(PostSql.COUNT_OF_POST_BY_WRITER_QUERY)
    fun countOfPostByWriter(@Param(PostSql.WRITER_ID) writerId: UUID): Long
}