package howru.howru.global.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

private object ResponseMessage {
    const val BAD_REQUEST = "잘못된 요청을 하였습니다. 필요한 파라미터가 존재하지 않습니다."
}

object GlobalResponse {
    fun badRequest() =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ResponseMessage.BAD_REQUEST)
}
