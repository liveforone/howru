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
import howru.howru.globalUtil.DEFAULT_PAGE
import howru.howru.globalUtil.PAGE
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
    fun getCommentDetailInfo(@PathVariable(CommentsParam.ID) @Positive id: Long): ResponseEntity<CommentsInfo> {
        val comment = commentsQueryService.getCommentById(id)
        return ResponseEntity.ok(comment)
    }

    @GetMapping(CommentsUrl.COMMENTS_BY_WRITER)
    fun getCommentsByWriterPage(
        @PathVariable(CommentsParam.WRITER_ID) writerId: UUID,
        @RequestParam(PAGE, required = false) page: Int = DEFAULT_PAGE
    ): ResponseEntity<List<CommentsInfo>> {
        val comments = commentsQueryService.getCommentsByWriter(writerId, page)
        return ResponseEntity.ok(comments)
    }

    @GetMapping(CommentsUrl.COMMENTS_BY_POST)
    fun getCommentsByPostPage(
        @PathVariable(CommentsParam.POST_ID) postId: Long,
        @RequestParam(PAGE, required = false) page: Int = DEFAULT_PAGE
    ): ResponseEntity<List<CommentsInfo>> {
        val comments = commentsQueryService.getCommentsByPost(postId, page)
        return ResponseEntity.ok(comments)
    }

    @GetMapping(CommentsUrl.COMMENTS_BY_SOMEONE)
    fun getCommentsBySomeonePage(
        @PathVariable(CommentsParam.WRITER_ID) writerId: UUID,
        @RequestParam(CommentsParam.MEMBER_ID) memberId: UUID,
        @RequestParam(PAGE, required = false) page: Int = DEFAULT_PAGE
    ): ResponseEntity<List<CommentsInfo>> {
        val comments = commentsQueryService.getCommentsBySomeone(writerId, memberId, page)
        return ResponseEntity.ok(comments)
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