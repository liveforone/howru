package howru.howru.post.controller

import howru.howru.logger
import howru.howru.post.log.PostControllerLog
import howru.howru.post.controller.constant.PostParam
import howru.howru.post.controller.constant.PostUrl
import howru.howru.post.controller.response.PostResponse
import howru.howru.post.dto.request.CreatePost
import howru.howru.post.dto.request.RemovePost
import howru.howru.post.dto.request.UpdatePostContent
import howru.howru.post.dto.response.PostInfo
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
    fun getDetailInfo(@PathVariable(PostParam.ID) @Positive id: Long): ResponseEntity<PostInfo> {
        val postDetail = postQueryService.getPostById(id)
        return PostResponse.postDetailSuccess(postDetail)
    }

    @GetMapping(PostUrl.MY_POST)
    fun getMyPostPage(
        @PathVariable(PostParam.MEMBER_ID) memberId: UUID,
        @RequestParam(PostParam.LAST_ID, required = false) lastId: Long?
    ): ResponseEntity<List<PostInfo>> {
        val myPosts = postQueryService.getMyPosts(memberId, lastId)
        return ResponseEntity.ok(myPosts)
    }

    @GetMapping(PostUrl.ALL_POST)
    fun getAllPostPage(@RequestParam(PostParam.LAST_ID, required = false) lastId: Long?): ResponseEntity<List<PostInfo>> {
        val allPosts = postQueryService.getAllPosts(lastId)
        return ResponseEntity.ok(allPosts)
    }

    @GetMapping(PostUrl.POST_OF_WRITER)
    fun getPostOfWriterPage(
        @PathVariable(PostParam.WRITER_ID) writerId: UUID,
        @RequestParam(PostParam.MEMBER_ID) memberId: UUID,
        @RequestParam(PostParam.LAST_ID, required = false) lastId: Long?
    ): ResponseEntity<List<PostInfo>> {
        val postsOfWriter = postQueryService.getPostsBySomeone(writerId, memberId, lastId)
        return ResponseEntity.ok(postsOfWriter)
    }

    @GetMapping(PostUrl.POST_OF_FOLLOWEE)
    fun getPostOfFolloweePage(
        @PathVariable(PostParam.FOLLOWER_ID) followerId: UUID,
        @RequestParam(PostParam.LAST_ID, required = false) lastId: Long?
    ): ResponseEntity<List<PostInfo>> {
        val postsOfFollowee = postQueryService.getPostsOfFollowee(followerId, lastId)
        return ResponseEntity.ok(postsOfFollowee)
    }

    @GetMapping(PostUrl.RECOMMEND)
    fun getRecommendPostPage(
        @RequestParam(PostParam.CONTENT) content: String,
        @RequestParam(PostParam.LAST_ID, required = false) lastId: Long?
    ): ResponseEntity<List<PostInfo>> {
        val recommendPosts = postQueryService.getRecommendPosts(content, lastId)
        return ResponseEntity.ok(recommendPosts)
    }

    @GetMapping(PostUrl.RANDOM)
    fun getRandomPostPage(): ResponseEntity<List<PostInfo>> {
        val randomPosts = postQueryService.getRandomPosts()
        return PostResponse.randomPostSuccess(randomPosts)
    }

    @GetMapping(PostUrl.COUNT_POST_BY_WRITER)
    fun getCountPostByWriterInfo(@PathVariable(PostParam.WRITER_ID) writerId: UUID): ResponseEntity<Long> {
        val countPost = postQueryService.getCountOfPostsByWriter(writerId)
        return PostResponse.countPostOfWriterSuccess(countPost)
    }

    @PostMapping(PostUrl.CREATE)
    fun createPost(
        @RequestBody @Valid createPost: CreatePost
    ): ResponseEntity<String> {
        postCommandService.createPost(createPost)
        logger().info(PostControllerLog.CREATE_POST_SUCCESS + createPost.writerId)

        return PostResponse.createPostSuccess()
    }

    @PatchMapping(PostUrl.EDIT_CONTENT)
    fun editPostContent(
        @PathVariable(PostParam.ID) @Positive id: Long,
        @RequestBody @Valid updatePostContent: UpdatePostContent
    ): ResponseEntity<String> {
        postCommandService.editPostContent(id, updatePostContent)
        logger().info(PostControllerLog.EDIT_CONTENT_SUCCESS + id)

        return PostResponse.editPostSuccess()
    }

    @DeleteMapping(PostUrl.REMOVE)
    fun removePost(
        @PathVariable(PostParam.ID) @Positive id: Long,
        @RequestBody @Valid removePost: RemovePost,
    ): ResponseEntity<String> {
        postCommandService.removePost(id, removePost)
        logger().info(PostControllerLog.DELETE_POST_SUCCESS + id)

        return PostResponse.removePostSuccess()
    }
}