package howru.howru.post.controller.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

private object ResponseMessage {
    const val CREATE_SUCCESS = "게시글을 성공적으로 등록하였습니다."
    const val EDIT_CONTENT_SUCCESS = "게시글을 성공적으로 수정하였습니다."
    const val REMOVE_SUCCESS = "게시글을 성공적으로 삭제하였습니다."
}

object PostResponse {
    fun createPostSuccess() = ResponseEntity.status(HttpStatus.CREATED).body(ResponseMessage.CREATE_SUCCESS)

    fun editPostSuccess() = ResponseEntity.ok(ResponseMessage.EDIT_CONTENT_SUCCESS)

    fun removePostSuccess() = ResponseEntity.ok(ResponseMessage.REMOVE_SUCCESS)
}
