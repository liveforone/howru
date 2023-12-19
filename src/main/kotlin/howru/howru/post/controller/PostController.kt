package howru.howru.post.controller

import howru.howru.globalUtil.validateBinding
import howru.howru.logger
import howru.howru.post.log.PostControllerLog
import howru.howru.post.controller.constant.PostParam
import howru.howru.post.controller.constant.PostUrl
import howru.howru.post.controller.response.PostResponse
import howru.howru.post.dto.request.CreatePost
import howru.howru.post.dto.request.RemovePost
import howru.howru.post.dto.request.UpdatePostContent
import howru.howru.post.service.command.PostCommandService
import howru.howru.post.service.query.PostQueryService
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
class PostController @Autowired constructor(
    private val postQueryService: PostQueryService,
    private val postCommandService: PostCommandService
) {
    @GetMapping(PostUrl.DETAIL)
    fun detail(@PathVariable(PostParam.ID) @Positive id: Long): ResponseEntity<*> {
        val postDetail = postQueryService.getPostById(id)
        return PostResponse.postDetailSuccess(postDetail)
    }

    @GetMapping(PostUrl.MY_POST)
    fun myPost(
        @PathVariable(PostParam.MEMBER_UUID) memberUUID: UUID,
        @RequestParam(PostParam.LAST_ID, required = false) lastId: Long?
    ): ResponseEntity<*> {
        val myPosts = postQueryService.getMyPosts(memberUUID, lastId)
        return PostResponse.myPostSuccess(myPosts)
    }

    @GetMapping(PostUrl.ALL_POST)
    fun allPost(@RequestParam(PostParam.LAST_ID, required = false) lastId: Long?): ResponseEntity<*> {
        val allPosts = postQueryService.getAllPosts(lastId)
        return PostResponse.allPostSuccess(allPosts)
    }

    @GetMapping(PostUrl.POST_OF_WRITER)
    fun postOfWriter(
        @PathVariable(PostParam.WRITER_UUID) writerUUID: UUID,
        @RequestParam(PostParam.MEMBER_UUID) memberUUID: UUID,
        @RequestParam(PostParam.LAST_ID, required = false) lastId: Long?
    ): ResponseEntity<*> {
        val postsOfWriter = postQueryService.getPostsBySomeone(writerUUID, memberUUID, lastId)
        return PostResponse.postOfWriterSuccess(postsOfWriter)
    }

    @GetMapping(PostUrl.POST_OF_FOLLOWEE)
    fun postOfFollowee(
        @PathVariable(PostParam.FOLLOWER_UUID) followerUUID: UUID,
        @RequestParam(PostParam.LAST_ID, required = false) lastId: Long?
    ): ResponseEntity<*> {
        val postsOfFollowee = postQueryService.getPostsOfFollowee(followerUUID, lastId)
        return PostResponse.postOfFolloweeSuccess(postsOfFollowee)
    }

    @GetMapping(PostUrl.RECOMMEND)
    fun recommendPost(@RequestParam(PostParam.CONTENT) content: String): ResponseEntity<*> {
        val recommendPosts = postQueryService.getRecommendPosts(content)
        return PostResponse.recommendPostSuccess(recommendPosts)
    }

    @GetMapping(PostUrl.RANDOM)
    fun randomPost(): ResponseEntity<*> {
        val randomPosts = postQueryService.getRandomPosts()
        return PostResponse.randomPostSuccess(randomPosts)
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
        logger().info(PostControllerLog.CREATE_POST_SUCCESS + createPost.writerUUID)

        return PostResponse.createPostSuccess()
    }

    @PatchMapping(PostUrl.EDIT_CONTENT)
    fun editContent(
        @PathVariable(PostParam.ID) @Positive id: Long,
        @RequestBody @Valid updatePostContent: UpdatePostContent,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        postCommandService.editContent(id, updatePostContent)
        logger().info(PostControllerLog.EDIT_CONTENT_SUCCESS + id)

        return PostResponse.editPostSuccess()
    }

    @DeleteMapping(PostUrl.REMOVE)
    fun removePost(
        @PathVariable(PostParam.ID) @Positive id: Long,
        @RequestBody @Valid removePost: RemovePost,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        postCommandService.removePost(id, removePost)
        logger().info(PostControllerLog.DELETE_POST_SUCCESS + id)

        return PostResponse.removePostSuccess()
    }
}