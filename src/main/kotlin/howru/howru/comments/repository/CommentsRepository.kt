package howru.howru.comments.repository

import howru.howru.comments.domain.Comments
import org.springframework.data.jpa.repository.JpaRepository

interface CommentsRepository :
    JpaRepository<Comments, Long>,
    CommentsCustomRepository
