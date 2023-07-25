package howru.howru.post.controller.response

import howru.howru.post.dto.response.PostInfo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object PostResponse {
    private const val CREATE_POST_SUCCESS = "게시글을 성공적으로 등록하였습니다."
    private const val EDIT_CONTENT_SUCCESS = "게시글을 성공적으로 수정하였습니다."
    private const val DELETE_POST_SUCCESS = "게시글을 성공적으로 삭제하였습니다."

    fun postDetailSuccess(postDetail: PostInfo) = ResponseEntity.ok(postDetail)
    fun myPostSuccess(myPosts: List<PostInfo>) = ResponseEntity.ok(myPosts)
    fun allPostSuccess(allPosts: List<PostInfo>) = ResponseEntity.ok(allPosts)
    fun postOfWriterSuccess(postsOfWriter: List<PostInfo>) = ResponseEntity.ok(postsOfWriter)
    fun postOfFolloweeSuccess(postsOfFollowee: List<PostInfo>) = ResponseEntity.ok(postsOfFollowee)
    fun recommendPostSuccess(recommendPosts: List<PostInfo>) = ResponseEntity.ok(recommendPosts)
    fun countPostOfWriterSuccess(countPost: Long) = ResponseEntity.ok(countPost)
    fun createPostSuccess() = ResponseEntity.status(HttpStatus.CREATED).body(CREATE_POST_SUCCESS)
    fun editPostSuccess() = ResponseEntity.ok(EDIT_CONTENT_SUCCESS)
    fun deletePostSuccess() = ResponseEntity.ok(DELETE_POST_SUCCESS)
}