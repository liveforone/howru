package howru.howru.likes.controller.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object LikesResponse {
    private const val LIKE_SUCCESS = "좋아요 등록에 성공하였습니다."
    private const val DISLIKE_SUCCESS = "좋아요 취소에 성공하였습니다."

    fun likeSuccess() = ResponseEntity.status(HttpStatus.CREATED).body(LIKE_SUCCESS)

    fun dislikeSuccess() = ResponseEntity.ok(DISLIKE_SUCCESS)
}
