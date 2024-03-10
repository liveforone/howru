package howru.howru.reply.repository

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import howru.howru.reply.domain.Reply
import org.springframework.data.jpa.repository.JpaRepository

interface ReplyRepository : JpaRepository<Reply, Long>, KotlinJdslJpqlExecutor