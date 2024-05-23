package howru.howru.comments.controller

import howru.howru.comments.controller.constant.CommentsParam
import howru.howru.comments.controller.constant.CommentsUrl
import howru.howru.comments.controller.response.CommentsResponse
import howru.howru.comments.dto.request.CreateComments
import howru.howru.comments.dto.request.RemoveComments
import howru.howru.comments.dto.request.UpdateComments
import howru.howru.comments.dto.response.CommentsInfo
import howru.howru.comments.dto.response.CommentsPage
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
        fun commentsDetail(
            @PathVariable(CommentsParam.ID) @Positive id: Long
        ): ResponseEntity<CommentsInfo> {
            val comment = commentsQueryService.getCommentById(id)
            return ResponseEntity.ok(comment)
        }

        @GetMapping(CommentsUrl.COMMENTS_BY_POST)
        fun commentsByPost(
            @RequestParam(CommentsParam.POST_ID) postId: Long,
            @RequestParam(CommentsParam.LAST_ID, required = false) lastId: Long?
        ): ResponseEntity<CommentsPage> {
            val comments = commentsQueryService.getCommentsByPost(postId, lastId)
            return ResponseEntity.ok(comments)
        }

        @GetMapping(CommentsUrl.MY_COMMENTS)
        fun myComments(
            @PathVariable(CommentsParam.MEMBER_ID) memberId: UUID,
            @RequestParam(CommentsParam.LAST_ID, required = false) lastId: Long?
        ): ResponseEntity<CommentsPage> {
            val comments = commentsQueryService.getCommentsByWriter(memberId, lastId)
            return ResponseEntity.ok(comments)
        }

        @GetMapping(CommentsUrl.OTHER_COMMENTS)
        fun otherComments(
            @PathVariable(CommentsParam.MEMBER_ID) memberId: UUID,
            @RequestParam(CommentsParam.LAST_ID, required = false) lastId: Long?,
            principal: Principal
        ): ResponseEntity<CommentsPage> {
            val comments =
                commentsQueryService.getCommentsBySomeone(memberId, myId = UUID.fromString(principal.name), lastId)
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
