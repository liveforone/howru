package howru.howru.likes.controller

import howru.howru.global.response.GlobalResponse
import howru.howru.likes.controller.constant.LikesParam
import howru.howru.likes.controller.constant.LikesUrl
import howru.howru.likes.controller.response.LikesResponse
import howru.howru.likes.dto.request.CreateLikes
import howru.howru.likes.dto.request.RemoveLikes
import howru.howru.likes.log.LikesControllerLog
import howru.howru.likes.service.command.LikesCommandService
import howru.howru.likes.service.query.LikesQueryService
import howru.howru.logger
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
class LikesController
    @Autowired
    constructor(
        private val likesQueryService: LikesQueryService,
        private val likesCommandService: LikesCommandService
    ) {
        @GetMapping(LikesUrl.COUNT_OF_LIKES_IN_POST)
        fun getCountOfLikesByPostInfo(
            @PathVariable(LikesParam.POST_ID) postId: Long
        ): ResponseEntity<Long> {
            val countOfLikes = likesQueryService.getCountOfLikesByPost(postId)
            return ResponseEntity.ok(countOfLikes)
        }

        @GetMapping(LikesUrl.LIKES_PAGE)
        fun likesBelongMember(
            @RequestParam(LikesParam.MEMBER_ID, required = false) memberId: UUID?,
            @RequestParam(LikesParam.POST_ID, required = false) postId: Long?,
            @RequestParam(LikesParam.LAST_TIMESTAMP, required = false) lastTimestamp: Int?
        ): ResponseEntity<*> {
            val likes =
                when {
                    memberId != null -> likesQueryService.getLikesBelongMember(memberId, lastTimestamp)
                    postId != null -> likesQueryService.getLikesBelongPost(postId, lastTimestamp)
                    else -> return GlobalResponse.badRequest()
                }
            return ResponseEntity.ok(likes)
        }

        @PostMapping(LikesUrl.LIKE)
        fun like(
            @RequestBody @Valid createLikes: CreateLikes
        ): ResponseEntity<String> {
            likesCommandService.createLikes(createLikes)
            logger().info(LikesControllerLog.CREATE_LIKE_SUCCESS + createLikes.postId)

            return LikesResponse.likeSuccess()
        }

        @DeleteMapping(LikesUrl.DISLIKE)
        fun dislike(
            @RequestBody @Valid removeLikes: RemoveLikes
        ): ResponseEntity<String> {
            likesCommandService.removeLikes(removeLikes)
            logger().info(LikesControllerLog.DELETE_LIKE_SUCCESS + removeLikes.postId)

            return LikesResponse.dislikeSuccess()
        }
    }
