package howru.howru.reply.controller.response

import howru.howru.reply.dto.response.ReplyInfo
import org.springframework.http.ResponseEntity

object ReplyResponse {
    private const val CREATE_REPLY_SUCCESS = "대댓글을 성공적으로 등록하였습니다."
    private const val EDIT_REPLY_SUCCESS = "대댓글을 성공적으로 수정하였습니다."
    private const val DELETE_REPLY_SUCCESS = "대댓글을 성공적으로 삭제하였습니다."

    fun detailSuccess(reply: ReplyInfo) = ResponseEntity.ok(reply)
    fun belongWriterSuccess(replies: List<ReplyInfo>) = ResponseEntity.ok(replies)
    fun belongCommentSuccess(replies: List<ReplyInfo>) = ResponseEntity.ok(replies)
    fun createReplySuccess() = ResponseEntity.ok(CREATE_REPLY_SUCCESS)
    fun editReplySuccess() = ResponseEntity.ok(EDIT_REPLY_SUCCESS)
    fun deleteReplySuccess() = ResponseEntity.ok(DELETE_REPLY_SUCCESS)
}