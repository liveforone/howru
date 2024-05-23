package howru.howru.comments.controller

import howru.howru.comments.controller.constant.CommentsParam
import howru.howru.comments.controller.constant.CommentsUrl
import howru.howru.comments.controller.response.CommentsResponse
import howru.howru.comments.dto.request.CreateComments
import howru.howru.comments.dto.request.RemoveComments
import howru.howru.comments.dto.request.UpdateComments
import howru.howru.comments.dto.response.CommentsInfo
import howru.howru.comments.log.CommentsControllerLog
import howru.howru.comments.service.command.CommentsCommandService
import howru.howru.comments.service.query.CommentsQueryService
import howru.howru.logger
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.UUID

@RestController
class CommentsController
    @Autowired
    constructor(
        private val commentsQueryService: CommentsQueryService,
        private val commentsCommandService: CommentsCommandService
    ) {
        @GetMapping(CommentsUrl.DETAIL)
        fun getCommentDetailInfo(
            @PathVariable(CommentsParam.ID) @Positive id: Long
        ): ResponseEntity<CommentsInfo> {
            val comment = commentsQueryService.getCommentById(id)
            return ResponseEntity.ok(comment)
        }

        @GetMapping(CommentsUrl.COMMENTS_PAGE)
        fun getCommentsPage(
            @RequestParam(CommentsParam.WRITER_ID, required = false) writerId: UUID?,
            @RequestParam(CommentsParam.POST_ID, required = false) postId: Long?,
            @RequestParam(CommentsParam.MEMBER_ID, required = false) memberId: UUID?,
            @RequestParam(CommentsParam.LAST_ID, required = false) lastId: Long?,
            principal: Principal
        ): ResponseEntity<*> {
            val comments = when {
                writerId != null -> commentsQueryService.getCommentsByWriter(writerId, lastId)
                postId != null -> commentsQueryService.getCommentsByPost(postId, lastId)
                memberId != null -> commentsQueryService.getCommentsBySomeone(
                    memberId, myId = UUID.fromString(principal.name), lastId
                )
                else -> return CommentsResponse.badRequestCommentsPage()
            }
            return ResponseEntity.ok(comments)
        }

        @PostMapping(CommentsUrl.CREATE_COMMENTS)
        fun createComments(
            @RequestBody @Valid createComments: CreateComments
        ): ResponseEntity<String> {
            commentsCommandService.createComments(createComments)
            logger().info(CommentsControllerLog.CREATE_COMMENTS_SUCCESS + createComments.writerId)

            return CommentsResponse.createCommentsSuccess()
        }

        @PatchMapping(CommentsUrl.EDIT_COMMENTS)
        fun editComments(
            @PathVariable(CommentsParam.ID) @Positive id: Long,
            @RequestBody @Valid updateComments: UpdateComments
        ): ResponseEntity<String> {
            commentsCommandService.editComment(id, updateComments)
            logger().info(CommentsControllerLog.EDIT_COMMENTS_SUCCESS + id)

            return CommentsResponse.editCommentsSuccess()
        }

        @DeleteMapping(CommentsUrl.REMOVE_COMMENTS)
        fun removeComments(
            @PathVariable(CommentsParam.ID) @Positive id: Long,
            @RequestBody @Valid removeComments: RemoveComments
        ): ResponseEntity<String> {
            commentsCommandService.removeComment(id, removeComments)
            logger().info(CommentsControllerLog.DELETE_COMMENTS_SUCCESS + id)

            return CommentsResponse.removeCommentsSuccess()
        }
    }
