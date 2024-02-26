package howru.howru.comments.controller

import howru.howru.comments.log.CommentsControllerLog
import howru.howru.comments.controller.constant.CommentsParam
import howru.howru.comments.controller.constant.CommentsUrl
import howru.howru.comments.controller.response.CommentsResponse
import howru.howru.comments.dto.request.CreateComments
import howru.howru.comments.dto.request.RemoveComments
import howru.howru.comments.dto.request.UpdateComments
import howru.howru.comments.dto.response.CommentsInfo
import howru.howru.comments.service.command.CommentsCommandService
import howru.howru.comments.service.query.CommentsQueryService
import howru.howru.globalUtil.validateBinding
import howru.howru.logger
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
class CommentsController @Autowired constructor(
    private val commentsQueryService: CommentsQueryService,
    private val commentsCommandService: CommentsCommandService
) {
    @GetMapping(CommentsUrl.DETAIL)
    fun commentDetail(@PathVariable(CommentsParam.ID) @Positive id: Long): ResponseEntity<CommentsInfo> {
        val comment = commentsQueryService.getCommentById(id)
        return CommentsResponse.detailSuccess(comment)
    }

    @GetMapping(CommentsUrl.COMMENTS_BY_WRITER)
    fun commentsByWriter(
        @PathVariable(CommentsParam.WRITER_ID) writerId: UUID,
        @RequestParam(CommentsParam.LAST_ID, required = false) lastId: Long?
    ): ResponseEntity<List<CommentsInfo>> {
        val comments = commentsQueryService.getCommentsByWriter(writerId, lastId)
        return CommentsResponse.commentsByWriterSuccess(comments)
    }

    @GetMapping(CommentsUrl.COMMENTS_BY_POST)
    fun commentsByPost(
        @PathVariable(CommentsParam.POST_ID) postId: Long,
        @RequestParam(CommentsParam.LAST_ID, required = false) lastId: Long?
    ): ResponseEntity<List<CommentsInfo>> {
        val comments = commentsQueryService.getCommentsByPost(postId, lastId)
        return CommentsResponse.commentsByPostSuccess(comments)
    }

    @GetMapping(CommentsUrl.COMMENTS_BY_SOMEONE)
    fun commentsBySomeone(
        @PathVariable(CommentsParam.WRITER_ID) writerId: UUID,
        @RequestParam(CommentsParam.MEMBER_ID) memberId: UUID,
        @RequestParam(CommentsParam.LAST_ID, required = false) lastId: Long?
    ): ResponseEntity<List<CommentsInfo>> {
        val comments = commentsQueryService.getCommentsBySomeone(writerId, memberId, lastId)
        return CommentsResponse.commentsBySomeoneSuccess(comments)
    }

    @PostMapping(CommentsUrl.CREATE_COMMENTS)
    fun createComments(
        @RequestBody @Valid createComments: CreateComments,
        bindingResult: BindingResult
    ): ResponseEntity<String> {
        validateBinding(bindingResult)

        commentsCommandService.createComments(createComments)
        logger().info(CommentsControllerLog.CREATE_COMMENTS_SUCCESS + createComments.writerId)

        return CommentsResponse.createCommentsSuccess()
    }

    @PatchMapping(CommentsUrl.EDIT_COMMENTS)
    fun editComments(
        @PathVariable(CommentsParam.ID) @Positive id: Long,
        @RequestBody @Valid updateComments: UpdateComments,
        bindingResult: BindingResult
    ): ResponseEntity<String> {
        validateBinding(bindingResult)

        commentsCommandService.editComment(id, updateComments)
        logger().info(CommentsControllerLog.EDIT_COMMENTS_SUCCESS + id)

        return CommentsResponse.editCommentsSuccess()
    }

    @DeleteMapping(CommentsUrl.REMOVE_COMMENTS)
    fun removeComments(
        @PathVariable(CommentsParam.ID) @Positive id: Long,
        @RequestBody @Valid removeComments: RemoveComments,
        bindingResult: BindingResult
    ): ResponseEntity<String> {
        validateBinding(bindingResult)

        commentsCommandService.removeComment(id, removeComments)
        logger().info(CommentsControllerLog.DELETE_COMMENTS_SUCCESS + id)

        return CommentsResponse.removeCommentsSuccess()
    }
}