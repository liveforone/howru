package howru.howru.subscribe.controller

import howru.howru.globalUtil.validateBinding
import howru.howru.logger
import howru.howru.subscribe.log.SubscribeControllerLog
import howru.howru.subscribe.controller.constant.SubscribeParam
import howru.howru.subscribe.controller.constant.SubscribeUrl
import howru.howru.subscribe.controller.response.SubscribeResponse
import howru.howru.subscribe.dto.request.CreateSubscribe
import howru.howru.subscribe.dto.request.UnsubscribeRequest
import howru.howru.subscribe.service.command.SubscribeCommandService
import howru.howru.subscribe.service.query.SubscribeQueryService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class SubscribeController @Autowired constructor(
    private val subscribeQueryService: SubscribeQueryService,
    private val subscribeCommandService: SubscribeCommandService
) {
    @GetMapping(SubscribeUrl.GET_FOLLOWING)
    fun getFollowing(
        @PathVariable(SubscribeParam.FOLLOWER_UUID) followerUUID: UUID,
        @RequestParam(SubscribeParam.LAST_FOLLOWEE_UUID, required = false) lastFolloweeUUID: UUID?,
        @RequestParam(SubscribeParam.LAST_FOLLOWER_UUID, required = false) lastFollowerUUID: UUID?
    ): ResponseEntity<*> {
        val subscribes = subscribeQueryService.getSubscribesByFollower(followerUUID, lastFolloweeUUID, lastFollowerUUID)
        return SubscribeResponse.getFollowingSuccess(subscribes)
    }

    @GetMapping(SubscribeUrl.GET_FOLLOWER)
    fun getFollower(
        @PathVariable(SubscribeParam.FOLLOWEE_UUID) followeeUUID: UUID,
        @RequestParam(SubscribeParam.LAST_FOLLOWEE_UUID, required = false) lastFolloweeUUID: UUID?,
        @RequestParam(SubscribeParam.LAST_FOLLOWER_UUID, required = false) lastFollowerUUID: UUID?
    ): ResponseEntity<*> {
        val subscribes = subscribeQueryService.getSubscribesByFollowee(followeeUUID, lastFolloweeUUID, lastFollowerUUID)
        return SubscribeResponse.getFollowerSuccess(subscribes)
    }

    @GetMapping(SubscribeUrl.COUNT_FOLLOWING)
    fun countFollowing(@PathVariable(SubscribeParam.FOLLOWER_UUID) followerUUID: UUID): ResponseEntity<*> {
        val countOfSubscribes = subscribeQueryService.getCountSubscribes(followerUUID)
        return SubscribeResponse.countFollowingSuccess(countOfSubscribes)
    }

    @GetMapping(SubscribeUrl.COUNT_FOLLOWER)
    fun countFollower(@PathVariable(SubscribeParam.FOLLOWEE_UUID) followeeUUID: UUID): ResponseEntity<*> {
        val countOfFollower = subscribeQueryService.getCountFollower(followeeUUID)
        return SubscribeResponse.countFollowerSuccess(countOfFollower)
    }

    @PostMapping(SubscribeUrl.SUBSCRIBE)
    fun subscribe(
        @RequestBody @Valid createSubscribe: CreateSubscribe,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        subscribeCommandService.createSubscribe(createSubscribe)
        logger().info(SubscribeControllerLog.SUBSCRIBE_SUCCESS + createSubscribe.followerUUID + SubscribeControllerLog.FOLLOWEE_INSERT_LOG + createSubscribe.followeeUUID)

        return SubscribeResponse.subscribeSuccess()
    }

    @DeleteMapping(SubscribeUrl.UNSUBSCRIBE)
    fun unsubscribe(
        @RequestBody @Valid unsubscribeRequest: UnsubscribeRequest,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        validateBinding(bindingResult)

        subscribeCommandService.unsubscribe(unsubscribeRequest)
        logger().info(SubscribeControllerLog.UNSUBSCRIBE_SUCCESS + unsubscribeRequest.followerUUID + SubscribeControllerLog.FOLLOWEE_INSERT_LOG + unsubscribeRequest.followeeUUID)

        return SubscribeResponse.unsubscribeSuccess()
    }
}