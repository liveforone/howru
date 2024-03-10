package howru.howru.comments.repository

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import howru.howru.comments.domain.Comments
import org.springframework.data.jpa.repository.JpaRepository

interface CommentsRepository : JpaRepository<Comments, Long>, KotlinJdslJpqlExecutor