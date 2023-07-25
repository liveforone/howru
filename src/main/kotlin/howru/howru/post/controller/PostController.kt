package howru.howru.post.controller

import howru.howru.globalUtil.validateBinding
import howru.howru.logger
import howru.howru.post.controller.constant.PostControllerLog
import howru.howru.post.controller.constant.PostParam
import howru.howru.post.controller.constant.PostUrl
import howru.howru.post.controller.response.PostResponse
import howru.howru.post.dto.request.CreatePost
import howru.howru.post.dto.request.DeletePost
import howru.howru.post.dto.update.UpdatePostContent
import howru.howru.post.service.command.PostCommandService
import howru.howru.post.service.query.PostQueryService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class PostController @Autowired constructor(
    private val postQueryService: PostQueryService,
    private val postCommandService: PostCommandService
) {
    @GetMapping(PostUrl.DETAIL)
    fun detail(@PathVariable(PostParam.UUID) uuid: UUID): ResponseEntity<*> {
        val postDetail = postQueryService.getPostByUUID(uuid)
        return PostResponse.postDetailSuccess(postDetail)
    }

    @GetMapping(PostUrl.MY_POST)
    fun myPost(
        @PathVariable(PostParam.MEMBER_UUID) memberUUID: UUID,
        @RequestParam(PostParam.LAST_UUID, required = false) lastUUID: UUID?
    ): ResponseEntity<*> {
        val myPosts = postQueryService.getMyPosts(memberUUID, lastUUID)
        return PostResponse.myPostSuccess(myPosts)
    }

    @GetMapping(PostUrl.ALL_POST)
    fun allPost(@RequestParam(PostParam.LAST_UUID, required = false) lastUUID: UUID?): ResponseEntity<*> {
        val allPosts = postQueryService.getAllPosts(lastUUID)
        return PostResponse.allPostSuccess(allPosts)
    }

    @GetMapping(PostUrl.POST_OF_WRITER)
    fun postOfWriter(
        @PathVariable(PostParam.WRITER_UUID) writerUUID: UUID,
        @RequestParam(PostParam.MEMBER_UUID) memberUUID: UUID,
        @RequestParam(PostParam.LAST_UUID, required = false) lastUUID: UUID?
    ): ResponseEntity<*> {
        val postsOfWriter = postQueryService.getPostsBySomeone(writerUUID, memberUUID, lastUUID)
        return PostResponse.postOfWriterSuccess(postsOfWriter)
    }

    @GetMapping(PostUrl.POST_OF_FOLLOWEE)
    fun postOfFollowee(
        @PathVariable(PostParam.FOLLOWER_UUID) followerUUID: UUID,
        @RequestParam(PostParam.LAST_UUID, required = false) lastUUID: UUID?
    ): ResponseEntity<*> {
        val postsOfFollowee = postQueryService.getPostsOfFollowee(followerUUID, lastUUID)
        return PostResponse.postOfFolloweeSuccess(postsOfFollowee)
    }

    @GetMapping(PostUrl.RECOMMEND)
    fun recommendPost(@RequestParam(PostParam.CONTENT) content: String): ResponseEntity<*> {
        val recommendPosts = postQueryService.getRecommendPosts(content)
        return PostResponse.recommendPostSuccess(recommendPosts)
    }

    @GetMapping(PostUrl.COUNT_POST_BY_WRITER)
    fun countPostByWriter(@PathVariable(PostParam.WRITER_UUID) writerUUID: UUID): ResponseEntity<*> {
        val countPost = postQueryService.countPostsByWriter(writerUUID)
        return PostResponse.countPostOfWriterSuccess(countPost)
    }

    @PostMapping(PostUrl.CREATE)
    fun createPost(
        @RequestBody @Valid createPost: CreatePost,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        postCommandService.createPost(createPost)
        logger().info(PostControllerLog.CREATE_POST_SUCCESS.log)

        return PostResponse.createPostSuccess()
    }

    @PutMapping(PostUrl.EDIT_CONTENT)
    fun editContent(
        @RequestBody @Valid updatePostContent: UpdatePostContent,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        postCommandService.editContent(updatePostContent)
        logger().info(PostControllerLog.EDIT_CONTENT_SUCCESS.log)

        return PostResponse.editPostSuccess()
    }

    @DeleteMapping(PostUrl.DELETE)
    fun deletePost(
        @RequestBody @Valid deletePost: DeletePost,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        postCommandService.deletePost(deletePost)
        logger().info(PostControllerLog.DELETE_POST_SUCCESS.log)

        return PostResponse.deletePostSuccess()
    }
}