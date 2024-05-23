package howru.howru.likes.controller.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

private object CommandSuccessMessage {
    const val LIKE_SUCCESS = "좋아요 등록에 성공하였습니다."
    const val DISLIKE_SUCCESS = "좋아요 취소에 성공하였습니다."
}

object LikesResponse {
    fun likeSuccess() = ResponseEntity.status(HttpStatus.CREATED).body(CommandSuccessMessage.LIKE_SUCCESS)

    fun dislikeSuccess() = ResponseEntity.ok(CommandSuccessMessage.DISLIKE_SUCCESS)
}
