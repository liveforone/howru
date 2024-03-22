package howru.howru.reply.repository

import howru.howru.reply.domain.Reply
import org.springframework.data.jpa.repository.JpaRepository

interface ReplyRepository : JpaRepository<Reply, Long>, ReplyCustomRepository