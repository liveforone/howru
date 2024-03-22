package howru.howru.reply.controller.response

import org.springframework.http.ResponseEntity

object ReplyResponse {
    private const val CREATE_SUCCESS = "대댓글을 성공적으로 등록하였습니다."
    private const val EDIT_SUCCESS = "대댓글을 성공적으로 수정하였습니다."
    private const val REMOVE_SUCCESS = "대댓글을 성공적으로 삭제하였습니다."

    fun createReplySuccess() = ResponseEntity.ok(CREATE_SUCCESS)
    fun editReplySuccess() = ResponseEntity.ok(EDIT_SUCCESS)
    fun removeReplySuccess() = ResponseEntity.ok(REMOVE_SUCCESS)
}