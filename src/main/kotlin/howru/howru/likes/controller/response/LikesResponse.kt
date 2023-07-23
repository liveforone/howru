package howru.howru.likes.controller.response

import howru.howru.likes.dto.response.LikesBelongMemberInfo
import howru.howru.likes.dto.response.LikesBelongPostInfo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object LikesResponse {
    private const val LIKE_SUCCESS = "좋아요 등록에 성공하였습니다."
    private const val DISLIKE_SUCCESS = "좋아요 취소에 성공하였습니다."

    fun belongMemberSuccess(likes: List<LikesBelongMemberInfo>) = ResponseEntity.ok(likes)
    fun belongPostSuccess(likes: List<LikesBelongPostInfo>) = ResponseEntity.ok(likes)
    fun likeSuccess() = ResponseEntity.status(HttpStatus.CREATED).body(LIKE_SUCCESS)
    fun dislikeSuccess() = ResponseEntity.ok(DISLIKE_SUCCESS)
}