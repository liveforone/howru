package howru.howru.reply.controller

import howru.howru.logger
import howru.howru.reply.controller.constant.ReplyParam
import howru.howru.reply.controller.constant.ReplyUrl
import howru.howru.reply.controller.response.ReplyResponse
import howru.howru.reply.dto.CreateReply
import howru.howru.reply.dto.RemoveReply
import howru.howru.reply.dto.UpdateReplyContent
import howru.howru.reply.domain.vo.ReplyInfo
import howru.howru.reply.log.ReplyControllerLog
import howru.howru.reply.service.command.ReplyCommandService
import howru.howru.reply.service.query.ReplyQueryService
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
class ReplyController
    @Autowired
    constructor(
        private val replyQueryService: ReplyQueryService,
        private val replyCommandService: ReplyCommandService
    ) {
        @GetMapping(ReplyUrl.DETAIL)
        fun getReplyDetailInfo(
            @PathVariable(ReplyParam.ID) @Positive id: Long
        ): ResponseEntity<ReplyInfo> {
            val reply = replyQueryService.getReplyById(id)
            return ResponseEntity.ok(reply)
        }

        @GetMapping(ReplyUrl.BELONG_WRITER)
        fun getRepliesBelongWriterPage(
            @PathVariable(ReplyParam.WRITER_ID) writerId: UUID,
            @RequestParam(ReplyParam.LAST_ID, required = false) lastId: Long?
        ): ResponseEntity<*> {
            val replies = replyQueryService.getRepliesByWriter(writerId, lastId)
            return ResponseEntity.ok(replies)
        }

        @GetMapping(ReplyUrl.BELONG_COMMENT)
        fun getRepliesBelongCommentPage(
            @PathVariable(ReplyParam.COMMENT_ID) commentId: Long,
            @RequestParam(ReplyParam.LAST_ID, required = false) lastId: Long?
        ): ResponseEntity<*> {
            val replies = replyQueryService.getRepliesByComment(commentId, lastId)
            return ResponseEntity.ok(replies)
        }

        @PostMapping(ReplyUrl.CREATE)
        fun createReply(
            @RequestBody @Valid createReply: CreateReply
        ): ResponseEntity<String> {
            replyCommandService.createReply(createReply)
            logger().info(ReplyControllerLog.CREATE_SUCCESS + createReply.writerId)

            return ReplyResponse.createReplySuccess()
        }

        @PatchMapping(ReplyUrl.EDIT)
        fun editReply(
            @PathVariable(ReplyParam.ID) @Positive id: Long,
            @RequestBody @Valid updateReplyContent: UpdateReplyContent
        ): ResponseEntity<String> {
            replyCommandService.editReply(id, updateReplyContent)
            logger().info(ReplyControllerLog.EDIT_SUCCESS + id)

            return ReplyResponse.editReplySuccess()
        }

        @DeleteMapping(ReplyUrl.REMOVE)
        fun removeReply(
            @PathVariable(ReplyParam.ID) @Positive id: Long,
            @RequestBody @Valid removeReply: RemoveReply
        ): ResponseEntity<String> {
            replyCommandService.removeReply(id, removeReply)
            logger().info(ReplyControllerLog.DELETE_SUCCESS + id)

            return ReplyResponse.removeReplySuccess()
        }
    }
