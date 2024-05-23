package howru.howru.post.controller.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

private object CommandSuccessMessage {
    const val CREATE_SUCCESS = "게시글을 성공적으로 등록하였습니다."
    const val EDIT_CONTENT_SUCCESS = "게시글을 성공적으로 수정하였습니다."
    const val REMOVE_SUCCESS = "게시글을 성공적으로 삭제하였습니다."
}

object PostResponse {
    fun createPostSuccess() = ResponseEntity.status(HttpStatus.CREATED).body(CommandSuccessMessage.CREATE_SUCCESS)

    fun editPostSuccess() = ResponseEntity.ok(CommandSuccessMessage.EDIT_CONTENT_SUCCESS)

    fun removePostSuccess() = ResponseEntity.ok(CommandSuccessMessage.REMOVE_SUCCESS)
}
