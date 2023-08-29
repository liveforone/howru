package howru.howru.likes.controller

import howru.howru.globalUtil.validateBinding
import howru.howru.likes.controller.constant.LikesControllerLog
import howru.howru.likes.controller.constant.LikesParam
import howru.howru.likes.controller.constant.LikesUrl
import howru.howru.likes.controller.response.LikesResponse
import howru.howru.likes.dto.request.CreateLikes
import howru.howru.likes.dto.request.DeleteLikes
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
    @GetMapping(LikesUrl.LIKES_BELONG_MEMBER)
    fun likesBelongMember(
        @PathVariable(LikesParam.MEMBER_UUID) memberUUID: UUID,
        @RequestParam(LikesParam.LAST_POST_ID, required = false) lastPostId: Long?
    ): ResponseEntity<*> {
        val likes = likesQueryService.getLikesBelongMember(memberUUID, lastPostId)
        return LikesResponse.belongMemberSuccess(likes)
    }

    @GetMapping(LikesUrl.LIKES_BELONG_POST)
    fun likesBelongPost(
        @PathVariable(LikesParam.POST_ID) postId: Long,
        @RequestParam(LikesParam.LAST_MEMBER_UUID, required = false) lastMemberUUID: UUID?
    ): ResponseEntity<*> {
        val likes = likesQueryService.getLikesBelongPost(postId, lastMemberUUID)
        return LikesResponse.belongPostSuccess(likes)
    }

    @PostMapping(LikesUrl.LIKE)
    fun like(
        @RequestBody @Valid createLikes: CreateLikes,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        likesCommandService.createLikes(createLikes)
        logger().info(LikesControllerLog.CREATE_LIKE_SUCCESS.log)

        return LikesResponse.likeSuccess()
    }

    @DeleteMapping(LikesUrl.DISLIKE)
    fun dislike(
        @RequestBody @Valid deleteLikes: DeleteLikes,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        likesCommandService.deleteLikes(deleteLikes)
        logger().info(LikesControllerLog.DELETE_LIKE_SUCCESS.log)

        return LikesResponse.dislikeSuccess()
    }
}