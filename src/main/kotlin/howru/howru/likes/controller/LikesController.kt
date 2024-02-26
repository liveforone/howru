package howru.howru.likes.controller

import howru.howru.globalUtil.validateBinding
import howru.howru.likes.log.LikesControllerLog
import howru.howru.likes.controller.constant.LikesParam
import howru.howru.likes.controller.constant.LikesUrl
import howru.howru.likes.controller.response.LikesResponse
import howru.howru.likes.dto.request.CreateLikes
import howru.howru.likes.dto.request.RemoveLikes
import howru.howru.likes.dto.response.LikesBelongMemberInfo
import howru.howru.likes.dto.response.LikesBelongPostInfo
import howru.howru.likes.service.command.LikesCommandService
import howru.howru.likes.service.query.LikesQueryService
import howru.howru.logger
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class LikesController @Autowired constructor(
    private val likesQueryService: LikesQueryService,
    private val likesCommandService: LikesCommandService
) {
    @GetMapping(LikesUrl.COUNT_OF_LIKES_IN_POST)
    fun getCountOfLikesByPostInfo(@PathVariable(LikesParam.POST_ID) postId: Long): ResponseEntity<Long> {
        val countOfLikes = likesQueryService.getCountOfLikesByPost(postId)
        return LikesResponse.countOfLikesInPostSuccess(countOfLikes)
    }

    @GetMapping(LikesUrl.LIKES_BELONG_MEMBER)
    fun getLikesBelongMemberPage(
        @PathVariable(LikesParam.MEMBER_ID) memberId: UUID,
        @RequestParam(LikesParam.LAST_POST_ID, required = false) lastPostId: Long?
    ): ResponseEntity<List<LikesBelongMemberInfo>> {
        val likes = likesQueryService.getLikesBelongMember(memberId, lastPostId)
        return LikesResponse.likesBelongMemberSuccess(likes)
    }

    @GetMapping(LikesUrl.LIKES_BELONG_POST)
    fun getLikesBelongPostPage(
        @PathVariable(LikesParam.POST_ID) postId: Long,
        @RequestParam(LikesParam.LAST_MEMBER_ID, required = false) lastMemberId: UUID?
    ): ResponseEntity<List<LikesBelongPostInfo>> {
        val likes = likesQueryService.getLikesBelongPost(postId, lastMemberId)
        return LikesResponse.likesBelongPostSuccess(likes)
    }

    @PostMapping(LikesUrl.LIKE)
    fun like(
        @RequestBody @Valid createLikes: CreateLikes,
        bindingResult: BindingResult
    ): ResponseEntity<String> {
        validateBinding(bindingResult)

        likesCommandService.createLikes(createLikes)
        logger().info(LikesControllerLog.CREATE_LIKE_SUCCESS + createLikes.postId)

        return LikesResponse.likeSuccess()
    }

    @DeleteMapping(LikesUrl.DISLIKE)
    fun dislike(
        @RequestBody @Valid removeLikes: RemoveLikes,
        bindingResult: BindingResult
    ): ResponseEntity<String> {
        validateBinding(bindingResult)

        likesCommandService.removeLikes(removeLikes)
        logger().info(LikesControllerLog.DELETE_LIKE_SUCCESS + removeLikes.postId)

        return LikesResponse.dislikeSuccess()
    }
}