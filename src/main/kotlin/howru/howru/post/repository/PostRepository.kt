package howru.howru.post.repository

import howru.howru.post.domain.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long>, PostCustomRepository