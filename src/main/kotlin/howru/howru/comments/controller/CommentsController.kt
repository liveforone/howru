package howru.howru.comments.controller

import howru.howru.comments.controller.constant.CommentsControllerLog
import howru.howru.comments.controller.constant.CommentsParam
import howru.howru.comments.controller.constant.CommentsUrl
import howru.howru.comments.controller.response.CommentsResponse
import howru.howru.comments.dto.request.CreateComments
import howru.howru.comments.dto.request.DeleteComments
import howru.howru.comments.dto.update.UpdateCommentsContent
import howru.howru.comments.service.command.CommentsCommandService
import howru.howru.comments.service.query.CommentsQueryService
import howru.howru.globalUtil.validateBinding
import howru.howru.logger
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
class CommentsController @Autowired constructor(
    private val commentsQueryService: CommentsQueryService,
    private val commentsCommandService: CommentsCommandService
) {
    @GetMapping(CommentsUrl.DETAIL)
    fun detail(@PathVariable(CommentsParam.UUID) uuid: UUID): ResponseEntity<*> {
        val comment = commentsQueryService.getCommentByUUID(uuid)
        return CommentsResponse.detailSuccess(comment)
    }

    @GetMapping(CommentsUrl.COMMENTS_BY_WRITER)
    fun commentsByWriter(
        @PathVariable(CommentsParam.WRITER_UUID) writerUUID: UUID,
        @RequestParam(CommentsParam.LAST_UUID, required = false) lastUUID: UUID?
    ): ResponseEntity<*> {
        val comments = commentsQueryService.getCommentsByWriter(writerUUID, lastUUID)
        return CommentsResponse.commentsByWriterSuccess(comments)
    }

    @GetMapping(CommentsUrl.COMMENTS_BY_POST)
    fun commentsByPost(
        @PathVariable(CommentsParam.POST_UUID) postUUID: UUID,
        @RequestParam(CommentsParam.LAST_UUID, required = false) lastUUID: UUID?
    ): ResponseEntity<*> {
        val comments = commentsQueryService.getCommentsByPost(postUUID, lastUUID)
        return CommentsResponse.commentsByPostSuccess(comments)
    }

    @PostMapping(CommentsUrl.CREATE)
    fun createComment(
        @RequestBody @Valid createComments: CreateComments,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        commentsCommandService.createComment(createComments)
        logger().info(CommentsControllerLog.CREATE_COMMENT_SUCCESS.log)

        return CommentsResponse.createCommentSuccess()
    }

    @PutMapping(CommentsUrl.EDIT)
    fun editComment(
        @RequestBody @Valid updateCommentsContent: UpdateCommentsContent,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        commentsCommandService.editContent(updateCommentsContent)
        logger().info(CommentsControllerLog.EDIT_COMMENT_SUCCESS.log)

        return CommentsResponse.editCommentSuccess()
    }

    @DeleteMapping(CommentsUrl.DELETE)
    fun deleteComment(
        @RequestBody @Valid deleteComments: DeleteComments,
         bindingResult: BindingResult
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        commentsCommandService.deleteComment(deleteComments)
        logger().info(CommentsControllerLog.DELETE_COMMENTS_SUCCESS.log)

        return CommentsResponse.deleteCommentSuccess()
    }
}