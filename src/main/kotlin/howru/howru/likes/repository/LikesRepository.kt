package howru.howru.likes.repository

import howru.howru.likes.domain.Likes
import howru.howru.likes.domain.LikesPk
import org.springframework.data.jpa.repository.JpaRepository

interface LikesRepository : JpaRepository<Likes, LikesPk>, LikesCustomRepository