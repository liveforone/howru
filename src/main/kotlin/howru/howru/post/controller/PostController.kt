package howru.howru.post.controller

import howru.howru.logger
import howru.howru.post.controller.constant.PostParam
import howru.howru.post.controller.constant.PostUrl
import howru.howru.post.controller.response.PostResponse
import howru.howru.post.dto.request.CreatePost
import howru.howru.post.dto.request.RemovePost
import howru.howru.post.dto.request.UpdatePostContent
import howru.howru.post.dto.response.PostInfo
import howru.howru.post.dto.response.PostPage
import howru.howru.post.log.PostControllerLog
import howru.howru.post.service.command.PostCommandService
import howru.howru.post.service.integrated.IntegratedPostService
import howru.howru.post.service.query.PostQueryService
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.UUID

@RestController
class PostController
    @Autowired
    constructor(
        private val postQueryService: PostQueryService,
        private val postCommandService: PostCommandService,
        private val integratedPostService: IntegratedPostService
    ) {
        @GetMapping(PostUrl.DETAIL)
        fun postDetail(
            @PathVariable(PostParam.ID) @Positive id: Long
        ): ResponseEntity<PostInfo> {
            val postDetail = postQueryService.getPostById(id)
            return ResponseEntity.ok(postDetail)
        }

        @GetMapping(PostUrl.ALL)
        fun allPost(
            @RequestParam(PostParam.LAST_ID, required = false) lastId: Long?
        ): ResponseEntity<PostPage> {
            val allPosts = postQueryService.getAllPosts(lastId)
            return ResponseEntity.ok(allPosts)
        }

        @GetMapping(PostUrl.POST_OF_OTHER_MEMBER, params = [PostParam.MEMBER_ID])
        fun postOfOtherMember(
            @RequestParam(PostParam.MEMBER_ID) memberId: UUID,
            @RequestParam(PostParam.LAST_ID, required = false) lastId: Long?,
            principal: Principal
        ): ResponseEntity<PostPage> {
            val posts = integratedPostService.getPostOfOtherMember(memberId, UUID.fromString(principal.name), lastId)
            return ResponseEntity.ok(posts)
        }

        @GetMapping(PostUrl.MY_POST)
        fun myPost(
            @RequestParam(PostParam.LAST_ID, required = false) lastId: Long?,
            principal: Principal
        ): ResponseEntity<PostPage> {
            val myPosts = postQueryService.getPostsByMember(UUID.fromString(principal.name), lastId)
            return ResponseEntity.ok(myPosts)
        }

        @GetMapping(PostUrl.POST_OF_FOLLOWEE)
        fun postOfFolloweePage(
            @RequestParam(PostParam.LAST_ID, required = false) lastId: Long?,
            principal: Principal
        ): ResponseEntity<PostPage> {
            val postsOfFollowee = integratedPostService.getPostsOfFollowee(UUID.fromString(principal.name), lastId)
            return ResponseEntity.ok(postsOfFollowee)
        }

        @GetMapping(PostUrl.COUNT_OF_POST, params = [PostParam.MEMBER_ID])
        fun countOfPostByMember(
            @RequestParam(PostParam.MEMBER_ID) memberId: UUID
        ): ResponseEntity<Long> {
            val countPost = postQueryService.getCountOfPostByMember(memberId)
            return ResponseEntity.ok(countPost)
        }

        @GetMapping(PostUrl.RECOMMEND, params = [PostParam.CONTENT])
        fun recommendPostPage(
            @RequestParam(PostParam.CONTENT) content: String,
            @RequestParam(PostParam.LAST_ID, required = false) lastId: Long?
        ): ResponseEntity<PostPage> {
            val recommendPosts = postQueryService.getRecommendPosts(content, lastId)
            return ResponseEntity.ok(recommendPosts)
        }

        @GetMapping(PostUrl.RANDOM)
        fun randomPostPage(): ResponseEntity<List<PostInfo>> {
            val randomPosts = postQueryService.getRandomPosts()
            return ResponseEntity.ok(randomPosts)
        }

        @PostMapping(PostUrl.CREATE)
        fun createPost(
            @RequestBody @Valid createPost: CreatePost
        ): ResponseEntity<String> {
            postCommandService.createPost(createPost)
            logger().info(PostControllerLog.CREATE_POST_SUCCESS + createPost.writerId)

            return PostResponse.createPostSuccess()
        }

        @PatchMapping(PostUrl.EDIT)
        fun editPost(
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
            @RequestBody @Valid removePost: RemovePost
        ): ResponseEntity<String> {
            postCommandService.removePost(id, removePost)
            logger().info(PostControllerLog.DELETE_POST_SUCCESS + id)

            return PostResponse.removePostSuccess()
        }
    }
