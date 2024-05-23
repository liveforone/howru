package howru.howru.subscribe.controller.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

private object ResponseMessage {
    const val SUBSCRIBE_SUCCESS = "구독을 성공적으로 완료하였습니다."
    const val UNSUBSCRIBE_SUCCESS = "구독 취소를 성공적으로 완료하였습니다."
}

object SubscribeResponse {
    fun subscribeSuccess() = ResponseEntity.status(HttpStatus.CREATED).body(ResponseMessage.SUBSCRIBE_SUCCESS)

    fun unsubscribeSuccess() = ResponseEntity.ok(ResponseMessage.UNSUBSCRIBE_SUCCESS)
}
