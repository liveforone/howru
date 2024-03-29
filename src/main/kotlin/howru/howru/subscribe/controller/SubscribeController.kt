package howru.howru.subscribe.controller

import howru.howru.globalUtil.validateBinding
import howru.howru.logger
import howru.howru.subscribe.controller.constant.SubscribeParam
import howru.howru.subscribe.controller.constant.SubscribeUrl
import howru.howru.subscribe.controller.response.SubscribeResponse
import howru.howru.subscribe.dto.request.CreateSubscribe
import howru.howru.subscribe.dto.request.UnsubscribeRequest
import howru.howru.subscribe.log.SubscribeControllerLog
import howru.howru.subscribe.service.command.SubscribeCommandService
import howru.howru.subscribe.service.query.SubscribeQueryService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class SubscribeController @Autowired constructor(
    private val subscribeQueryService: SubscribeQueryService,
    private val subscribeCommandService: SubscribeCommandService
) {
    @GetMapping(SubscribeUrl.GET_FOLLOWING)
    fun getFollowing(
        @PathVariable(SubscribeParam.FOLLOWER_ID) followerId: UUID,
        @RequestParam(SubscribeParam.LAST_TIMESTAMP, required = false) lastTimestamp: Int?
    ): ResponseEntity<*> {
        val subscribes = subscribeQueryService.getSubscribesByFollower(followerId, lastTimestamp)
        return ResponseEntity.ok(subscribes)
    }

    @GetMapping(SubscribeUrl.GET_FOLLOWER)
    fun getFollower(
        @PathVariable(SubscribeParam.FOLLOWEE_ID) followeeId: UUID,
        @RequestParam(SubscribeParam.LAST_TIMESTAMP, required = false) lastTimestamp: Int?
    ): ResponseEntity<*> {
        val subscribes = subscribeQueryService.getSubscribesByFollowee(followeeId, lastTimestamp)
        return ResponseEntity.ok(subscribes)
    }

    @GetMapping(SubscribeUrl.COUNT_FOLLOWING)
    fun getCountOfFollowingInfo(@PathVariable(SubscribeParam.FOLLOWER_ID) followerId: UUID): ResponseEntity<Long> {
        val countOfSubscribes = subscribeQueryService.getCountOfSubscribes(followerId)
        return SubscribeResponse.countFollowingSuccess(countOfSubscribes)
    }

    @GetMapping(SubscribeUrl.COUNT_FOLLOWER)
    fun getCountOfFollowerInfo(@PathVariable(SubscribeParam.FOLLOWEE_ID) followeeId: UUID): ResponseEntity<Long> {
        val countOfFollower = subscribeQueryService.getCountOfFollower(followeeId)
        return SubscribeResponse.countFollowerSuccess(countOfFollower)
    }

    @PostMapping(SubscribeUrl.SUBSCRIBE)
    fun subscribe(
        @RequestBody @Valid createSubscribe: CreateSubscribe,
        bindingResult: BindingResult
    ): ResponseEntity<String> {
        validateBinding(bindingResult)

        subscribeCommandService.createSubscribe(createSubscribe)
        logger().info(SubscribeControllerLog.SUBSCRIBE_SUCCESS + createSubscribe.followerId + SubscribeControllerLog.FOLLOWEE_INSERT_LOG + createSubscribe.followeeId)

        return SubscribeResponse.subscribeSuccess()
    }

    @DeleteMapping(SubscribeUrl.UNSUBSCRIBE)
    fun unsubscribe(
        @RequestBody @Valid unsubscribeRequest: UnsubscribeRequest,
        bindingResult: BindingResult
    ): ResponseEntity<String> {
        validateBinding(bindingResult)

        subscribeCommandService.unsubscribe(unsubscribeRequest)
        logger().info(SubscribeControllerLog.UNSUBSCRIBE_SUCCESS + unsubscribeRequest.followerId + SubscribeControllerLog.FOLLOWEE_INSERT_LOG + unsubscribeRequest.followeeId)

        return SubscribeResponse.unsubscribeSuccess()
    }
}