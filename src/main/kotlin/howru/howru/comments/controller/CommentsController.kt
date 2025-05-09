package howru.howru.comments.controller

import howru.howru.comments.controller.constant.CommentsApiDocs
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
import howru.howru.comments.service.integrated.IntegratedCommentsService
import howru.howru.comments.service.query.CommentsQueryService
import howru.howru.logger
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.UUID

@Tag(name = CommentsApiDocs.TAG_NAME)
@RestController
class CommentsController
    @Autowired
    constructor(
        private val commentsQueryService: CommentsQueryService,
        private val commentsCommandService: CommentsCommandService,
        private val integratedCommentsService: IntegratedCommentsService
    ) {
        @GetMapping(CommentsUrl.DETAIL)
        @Operation(summary = CommentsApiDocs.DETAIL_SUMMARY)
        fun commentsDetail(
            @PathVariable(CommentsParam.ID) @Positive id: Long
        ): ResponseEntity<CommentsInfo> {
            val comment = commentsQueryService.getCommentById(id)
            return ResponseEntity.ok(comment)
        }

        @GetMapping(CommentsUrl.COMMENTS_PAGE, params = [CommentsParam.POST_ID])
        @Operation(summary = CommentsApiDocs.BASIC_PAGE_SUMMARY, description = CommentsApiDocs.BASIC_PAGE_DESCRIPTION)
        fun commentsPage(
            @RequestParam(CommentsParam.POST_ID) postId: Long,
            @RequestParam(CommentsParam.LAST_ID, required = false) lastId: Long?
        ): ResponseEntity<CommentsPage> {
            val comments = commentsQueryService.getCommentsByPost(postId, lastId)
            return ResponseEntity.ok(comments)
        }

        @GetMapping(CommentsUrl.COMMENTS_OF_OTHER_MEMBER, params = [CommentsParam.MEMBER_ID])
        @Operation(summary = CommentsApiDocs.MEMBER_PAGE_SUMMARY, description = CommentsApiDocs.MEMBER_PAGE_DESCRIPTION)
        fun commentsOfOtherMember(
            @RequestParam(CommentsParam.MEMBER_ID) memberId: UUID,
            @RequestParam(CommentsParam.LAST_ID, required = false) lastId: Long?,
            principal: Principal
        ): ResponseEntity<CommentsPage> {
            val comments =
                integratedCommentsService.getCommentsByOtherMember(
                    memberId,
                    myId = UUID.fromString(principal.name),
                    lastId
                )
            return ResponseEntity.ok(comments)
        }

        @GetMapping(CommentsUrl.MY_COMMENTS)
        @Operation(summary = CommentsApiDocs.MY_PAGE_SUMMARY, description = CommentsApiDocs.MY_PAGE_DESCRIPTION)
        fun myComments(
            @RequestParam(CommentsParam.LAST_ID, required = false) lastId: Long?,
            principal: Principal
        ): ResponseEntity<CommentsPage> {
            val comments = commentsQueryService.getCommentsByMember(UUID.fromString(principal.name), lastId)
            return ResponseEntity.ok(comments)
        }

        @PostMapping(CommentsUrl.CREATE_COMMENTS)
        @Operation(summary = CommentsApiDocs.CREATE_SUMMARY)
        fun createComments(
            @RequestBody @Valid createComments: CreateComments
        ): ResponseEntity<String> {
            commentsCommandService.createComments(createComments)
            logger().info(CommentsControllerLog.CREATE_COMMENTS_SUCCESS + createComments.writerId)

            return CommentsResponse.createCommentsSuccess()
        }

        @PatchMapping(CommentsUrl.EDIT_COMMENTS)
        @Operation(summary = CommentsApiDocs.EDIT_SUMMARY)
        fun editComments(
            @PathVariable(CommentsParam.ID) @Positive id: Long,
            @RequestBody @Valid updateComments: UpdateComments
        ): ResponseEntity<String> {
            commentsCommandService.editComment(id, updateComments)
            logger().info(CommentsControllerLog.EDIT_COMMENTS_SUCCESS + id)

            return CommentsResponse.editCommentsSuccess()
        }

        @DeleteMapping(CommentsUrl.REMOVE_COMMENTS)
        @Operation(summary = CommentsApiDocs.REMOVE_SUMMARY)
        fun removeComments(
            @PathVariable(CommentsParam.ID) @Positive id: Long,
            @RequestBody @Valid removeComments: RemoveComments
        ): ResponseEntity<String> {
            commentsCommandService.removeComment(id, removeComments)
            logger().info(CommentsControllerLog.DELETE_COMMENTS_SUCCESS + id)

            return CommentsResponse.removeCommentsSuccess()
        }
    }
