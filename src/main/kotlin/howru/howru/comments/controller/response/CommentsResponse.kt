package howru.howru.comments.controller.response

import howru.howru.comments.dto.response.CommentsInfo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object CommentsResponse {
    private const val CREATE_COMMENTS_SUCCESS = "댓글 등록을 성공하였습니다."
    private const val EDIT_COMMENTS_SUCCESS = "댓글을 성공적으로 수정하였습니다."
    private const val REMOVE_COMMENTS_SUCCESS = "댓글을 성공적으로 삭제하였습니다."

    fun detailSuccess(comment: CommentsInfo) = ResponseEntity.ok(comment)
    fun commentsByWriterSuccess(comments: List<CommentsInfo>) = ResponseEntity.ok(comments)
    fun commentsByPostSuccess(comments: List<CommentsInfo>) = ResponseEntity.ok(comments)
    fun commentsBySomeoneSuccess(comments: List<CommentsInfo>) = ResponseEntity.ok(comments)
    fun createCommentsSuccess() = ResponseEntity.status(HttpStatus.CREATED).body(CREATE_COMMENTS_SUCCESS)
    fun editCommentsSuccess() = ResponseEntity.ok(EDIT_COMMENTS_SUCCESS)
    fun removeCommentsSuccess() = ResponseEntity.ok(REMOVE_COMMENTS_SUCCESS)
}