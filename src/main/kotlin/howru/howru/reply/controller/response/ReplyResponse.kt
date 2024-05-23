package howru.howru.reply.controller.response

import org.springframework.http.ResponseEntity

private object ResponseMessage {
    const val CREATE_SUCCESS = "대댓글을 성공적으로 등록하였습니다."
    const val EDIT_SUCCESS = "대댓글을 성공적으로 수정하였습니다."
    const val REMOVE_SUCCESS = "대댓글을 성공적으로 삭제하였습니다."
}

object ReplyResponse {
    fun createReplySuccess() = ResponseEntity.ok(ResponseMessage.CREATE_SUCCESS)

    fun editReplySuccess() = ResponseEntity.ok(ResponseMessage.EDIT_SUCCESS)

    fun removeReplySuccess() = ResponseEntity.ok(ResponseMessage.REMOVE_SUCCESS)
}
