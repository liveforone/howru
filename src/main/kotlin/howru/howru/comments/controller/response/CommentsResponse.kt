package howru.howru.comments.controller.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

private object ResponseMessage {
    const val BAD_REQUEST_COMMENTS_PAGE = "잘못된 요청을 하였습니다. 필요한 파라미터가 존재하지 않습니다."
    const val CREATE_COMMENTS_SUCCESS = "댓글 등록을 성공하였습니다."
    const val EDIT_COMMENTS_SUCCESS = "댓글을 성공적으로 수정하였습니다."
    const val REMOVE_COMMENTS_SUCCESS = "댓글을 성공적으로 삭제하였습니다."
}

object CommentsResponse {
    fun badRequestCommentsPage() =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ResponseMessage.BAD_REQUEST_COMMENTS_PAGE)

    fun createCommentsSuccess() =
        ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ResponseMessage.CREATE_COMMENTS_SUCCESS)

    fun editCommentsSuccess() = ResponseEntity.ok(ResponseMessage.EDIT_COMMENTS_SUCCESS)

    fun removeCommentsSuccess() = ResponseEntity.ok(ResponseMessage.REMOVE_COMMENTS_SUCCESS)
}
