package howru.howru.reply.controller

import howru.howru.globalUtil.validateBinding
import howru.howru.logger
import howru.howru.reply.controller.constant.ReplyControllerLog
import howru.howru.reply.controller.constant.ReplyParam
import howru.howru.reply.controller.constant.ReplyUrl
import howru.howru.reply.controller.response.ReplyResponse
import howru.howru.reply.dto.request.CreateReply
import howru.howru.reply.dto.request.DeleteReply
import howru.howru.reply.dto.update.UpdateReplyContent
import howru.howru.reply.service.command.ReplyCommandService
import howru.howru.reply.service.query.ReplyQueryService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class ReplyController @Autowired constructor(
    private val replyQueryService: ReplyQueryService,
    private val replyCommandService: ReplyCommandService
) {
    @GetMapping(ReplyUrl.DETAIL)
    fun detail(@PathVariable(ReplyParam.ID) id: Long): ResponseEntity<*> {
        val reply = replyQueryService.getReplyById(id)
        return ReplyResponse.detailSuccess(reply)
    }

    @GetMapping(ReplyUrl.BELONG_WRITER)
    fun belongWriter(
        @PathVariable(ReplyParam.WRITER_UUID) writerUUID: UUID,
        @RequestParam(ReplyParam.LAST_ID, required = false) lastId: Long?
    ): ResponseEntity<*> {
        val replies = replyQueryService.getRepliesByWriter(writerUUID, lastId)
        return ReplyResponse.belongWriterSuccess(replies)
    }

    @GetMapping(ReplyUrl.BELONG_COMMENT)
    fun belongComment(
        @PathVariable(ReplyParam.COMMENT_ID) commentId: Long,
        @RequestParam(ReplyParam.LAST_ID, required = false) lastId: Long?
    ): ResponseEntity<*> {
        val replies = replyQueryService.getRepliesByComment(commentId, lastId)
        return ReplyResponse.belongCommentSuccess(replies)
    }

    @PostMapping(ReplyUrl.CREATE)
    fun create(
        @RequestBody @Valid createReply: CreateReply,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        replyCommandService.createReply(createReply)
        logger().info(ReplyControllerLog.CREATE_SUCCESS.log)

        return ReplyResponse.createReplySuccess()
    }

    @PutMapping(ReplyUrl.EDIT)
    fun edit(
        @RequestBody @Valid updateReplyContent: UpdateReplyContent,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        replyCommandService.editReply(updateReplyContent)
        logger().info(ReplyControllerLog.EDIT_SUCCESS.log)

        return ReplyResponse.editReplySuccess()
    }

    @DeleteMapping(ReplyUrl.DELETE)
    fun delete(
        @RequestBody @Valid deleteReply: DeleteReply,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        replyCommandService.deleteReply(deleteReply)
        logger().info(ReplyControllerLog.DELETE_SUCCESS.log)

        return ReplyResponse.deleteReplySuccess()
    }
}