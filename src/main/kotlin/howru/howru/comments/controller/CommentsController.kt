package howru.howru.comments.controller

import howru.howru.comments.log.CommentsControllerLog
import howru.howru.comments.controller.constant.CommentsParam
import howru.howru.comments.controller.constant.CommentsUrl
import howru.howru.comments.controller.response.CommentsResponse
import howru.howru.comments.dto.request.CreateComments
import howru.howru.comments.dto.request.RemoveComments
import howru.howru.comments.dto.request.UpdateCommentsContent
import howru.howru.comments.service.command.CommentsCommandService
import howru.howru.comments.service.query.CommentsQueryService
import howru.howru.globalUtil.validateBinding
import howru.howru.logger
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
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
    fun detail(@PathVariable(CommentsParam.ID) @Positive id: Long): ResponseEntity<*> {
        val comment = commentsQueryService.getCommentById(id)
        return CommentsResponse.detailSuccess(comment)
    }

    @GetMapping(CommentsUrl.COMMENTS_BY_WRITER)
    fun commentsByWriter(
        @PathVariable(CommentsParam.WRITER_UUID) writerUUID: UUID,
        @RequestParam(CommentsParam.LAST_ID, required = false) lastId: Long?
    ): ResponseEntity<*> {
        val comments = commentsQueryService.getCommentsByWriter(writerUUID, lastId)
        return CommentsResponse.commentsByWriterSuccess(comments)
    }

    @GetMapping(CommentsUrl.COMMENTS_BY_POST)
    fun commentsByPost(
        @PathVariable(CommentsParam.POST_ID) postId: Long,
        @RequestParam(CommentsParam.LAST_ID, required = false) lastId: Long?
    ): ResponseEntity<*> {
        val comments = commentsQueryService.getCommentsByPost(postId, lastId)
        return CommentsResponse.commentsByPostSuccess(comments)
    }

    @GetMapping(CommentsUrl.COMMENTS_BY_SOMEONE)
    fun commentsBySomeone(
        @PathVariable(CommentsParam.WRITER_UUID) writerUUID: UUID,
        @RequestParam(CommentsParam.MEMBER_UUID) memberUUID: UUID,
        @RequestParam(CommentsParam.LAST_ID, required = false) lastId: Long?
    ): ResponseEntity<*> {
        val comments = commentsQueryService.getCommentsBySomeone(writerUUID, memberUUID, lastId)
        return CommentsResponse.commentsBySomeoneSuccess(comments)
    }

    @PostMapping(CommentsUrl.CREATE)
    fun createComment(
        @RequestBody @Valid createComments: CreateComments,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        commentsCommandService.createComment(createComments)
        logger().info(CommentsControllerLog.CREATE_COMMENT_SUCCESS + createComments.writerUUID)

        return CommentsResponse.createCommentSuccess()
    }

    @PutMapping(CommentsUrl.EDIT)
    fun editComment(
        @PathVariable(CommentsParam.ID) @Positive id: Long,
        @RequestBody @Valid updateCommentsContent: UpdateCommentsContent,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        commentsCommandService.editComment(id, updateCommentsContent)
        logger().info(CommentsControllerLog.EDIT_COMMENT_SUCCESS + id)

        return CommentsResponse.editCommentSuccess()
    }

    @DeleteMapping(CommentsUrl.REMOVE)
    fun removeComment(
        @PathVariable(CommentsParam.ID) @Positive id: Long,
        @RequestBody @Valid removeComments: RemoveComments,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        commentsCommandService.removeComment(id, removeComments)
        logger().info(CommentsControllerLog.DELETE_COMMENTS_SUCCESS + id)

        return CommentsResponse.removeCommentSuccess()
    }
}