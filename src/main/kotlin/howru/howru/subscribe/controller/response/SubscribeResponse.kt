package howru.howru.subscribe.controller.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object SubscribeResponse {
    private const val SUBSCRIBE_SUCCESS = "구독을 성공적으로 완료하였습니다."
    private const val UNSUBSCRIBE_SUCCESS = "구독 취소를 성공적으로 완료하였습니다."

    fun countFollowingSuccess(countOfFollowing: Long) = ResponseEntity.ok(countOfFollowing)
    fun countFollowerSuccess(countOfFollower: Long) = ResponseEntity.ok(countOfFollower)
    fun subscribeSuccess() = ResponseEntity.status(HttpStatus.CREATED).body(SUBSCRIBE_SUCCESS)
    fun unsubscribeSuccess() = ResponseEntity.ok(UNSUBSCRIBE_SUCCESS)
}