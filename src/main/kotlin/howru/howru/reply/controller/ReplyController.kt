package howru.howru.reply.controller

import howru.howru.logger
import howru.howru.reply.controller.constant.ReplyApiDocs
import howru.howru.reply.controller.constant.ReplyParam
import howru.howru.reply.controller.constant.ReplyUrl
import howru.howru.reply.controller.response.ReplyResponse
import howru.howru.reply.dto.request.CreateReply
import howru.howru.reply.dto.request.RemoveReply
import howru.howru.reply.dto.request.UpdateReplyContent
import howru.howru.reply.dto.response.ReplyInfo
import howru.howru.reply.dto.response.ReplyPage
import howru.howru.reply.log.ReplyControllerLog
import howru.howru.reply.service.command.ReplyCommandService
import howru.howru.reply.service.query.ReplyQueryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.UUID

@Tag(name = ReplyApiDocs.TAG_NAME)
@RestController
class ReplyController
    @Autowired
    constructor(
        private val replyQueryService: ReplyQueryService,
        private val replyCommandService: ReplyCommandService
    ) {
        @GetMapping(ReplyUrl.DETAIL)
        @Operation(summary = ReplyApiDocs.DETAIL_SUMMARY)
        fun replyDetail(
            @PathVariable(ReplyParam.ID) @Positive id: Long
        ): ResponseEntity<ReplyInfo> {
            val reply = replyQueryService.getReplyById(id)
            return ResponseEntity.ok(reply)
        }

        @GetMapping(ReplyUrl.REPLY_PAGE, params = [ReplyParam.COMMENT_ID])
        @Operation(summary = ReplyApiDocs.BASIC_PAGE_SUMMARY, description = ReplyApiDocs.BASIC_PAGE_DESCRIPTION)
        fun replyPage(
            @RequestParam(ReplyParam.COMMENT_ID) commentId: Long,
            @RequestParam(ReplyParam.LAST_ID, required = false) lastId: Long?
        ): ResponseEntity<ReplyPage> {
            val replies = replyQueryService.getRepliesByComment(commentId, lastId)
            return ResponseEntity.ok(replies)
        }

        @GetMapping(ReplyUrl.MY_REPLY)
        @Operation(summary = ReplyApiDocs.MY_PAGE_SUMMARY, description = ReplyApiDocs.MY_PAGE_DESCRIPTION)
        fun myReplies(
            @RequestParam(ReplyParam.LAST_ID, required = false) lastId: Long?,
            principal: Principal
        ): ResponseEntity<*> {
            val replies = replyQueryService.getRepliesByWriter(UUID.fromString(principal.name), lastId)
            return ResponseEntity.ok(replies)
        }

        @PostMapping(ReplyUrl.CREATE)
        @Operation(summary = ReplyApiDocs.CREATE_SUMMARY)
        fun createReply(
            @RequestBody @Valid createReply: CreateReply
        ): ResponseEntity<String> {
            replyCommandService.createReply(createReply)
            logger().info(ReplyControllerLog.CREATE_SUCCESS + createReply.writerId)

            return ReplyResponse.createReplySuccess()
        }

        @PatchMapping(ReplyUrl.EDIT)
        @Operation(summary = ReplyApiDocs.EDIT_SUMMARY)
        fun editReply(
            @PathVariable(ReplyParam.ID) @Positive id: Long,
            @RequestBody @Valid updateReplyContent: UpdateReplyContent
        ): ResponseEntity<String> {
            replyCommandService.editReply(id, updateReplyContent)
            logger().info(ReplyControllerLog.EDIT_SUCCESS + id)

            return ReplyResponse.editReplySuccess()
        }

        @DeleteMapping(ReplyUrl.REMOVE)
        @Operation(summary = ReplyApiDocs.REMOVE_SUMMARY)
        fun removeReply(
            @PathVariable(ReplyParam.ID) @Positive id: Long,
            @RequestBody @Valid removeReply: RemoveReply
        ): ResponseEntity<String> {
            replyCommandService.removeReply(id, removeReply)
            logger().info(ReplyControllerLog.DELETE_SUCCESS + id)

            return ReplyResponse.removeReplySuccess()
        }
    }
