package howru.howru.comments.controller.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

private object ResponseMessage {
    const val CREATE_COMMENTS_SUCCESS = "댓글 등록을 성공하였습니다."
    const val EDIT_COMMENTS_SUCCESS = "댓글을 성공적으로 수정하였습니다."
    const val REMOVE_COMMENTS_SUCCESS = "댓글을 성공적으로 삭제하였습니다."
}

object CommentsResponse {
    fun createCommentsSuccess() =
        ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ResponseMessage.CREATE_COMMENTS_SUCCESS)

    fun editCommentsSuccess() = ResponseEntity.ok(ResponseMessage.EDIT_COMMENTS_SUCCESS)

    fun removeCommentsSuccess() = ResponseEntity.ok(ResponseMessage.REMOVE_COMMENTS_SUCCESS)
}
