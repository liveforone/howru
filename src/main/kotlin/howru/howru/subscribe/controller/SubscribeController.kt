package howru.howru.subscribe.controller

import howru.howru.logger
import howru.howru.subscribe.controller.constant.SubscribeParam
import howru.howru.subscribe.controller.constant.SubscribeUrl
import howru.howru.subscribe.controller.response.SubscribeResponse
import howru.howru.subscribe.dto.request.CreateSubscribe
import howru.howru.subscribe.dto.request.UnsubscribeRequest
import howru.howru.subscribe.dto.response.SubscribeInfo
import howru.howru.subscribe.log.SubscribeControllerLog
import howru.howru.subscribe.service.command.SubscribeCommandService
import howru.howru.subscribe.service.query.SubscribeQueryService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class SubscribeController
    @Autowired
    constructor(
        private val subscribeQueryService: SubscribeQueryService,
        private val subscribeCommandService: SubscribeCommandService
    ) {
        @GetMapping(SubscribeUrl.FOLLOWING_INFO)
        fun followingInfo(
            @PathVariable(SubscribeParam.MEMBER_ID) memberId: UUID,
            @RequestParam(SubscribeParam.LAST_TIMESTAMP, required = false) lastTimestamp: Int?
        ): ResponseEntity<List<SubscribeInfo>> {
            val subscribes = subscribeQueryService.getFollowing(memberId, lastTimestamp)
            return ResponseEntity.ok(subscribes)
        }

        @GetMapping(SubscribeUrl.FOLLOWER_INFO)
        fun followerInfo(
            @PathVariable(SubscribeParam.MEMBER_ID) memberId: UUID,
            @RequestParam(SubscribeParam.LAST_TIMESTAMP, required = false) lastTimestamp: Int?
        ): ResponseEntity<List<SubscribeInfo>> {
            val subscribes = subscribeQueryService.getFollower(memberId, lastTimestamp)
            return ResponseEntity.ok(subscribes)
        }

        @GetMapping(SubscribeUrl.COUNT_FOLLOWING)
        fun countOfFollowing(
            @PathVariable(SubscribeParam.MEMBER_ID) memberId: UUID
        ): ResponseEntity<Long> {
            val countOfSubscribes = subscribeQueryService.getCountOfFollowing(memberId)
            return ResponseEntity.ok(countOfSubscribes)
        }

        @GetMapping(SubscribeUrl.COUNT_FOLLOWER)
        fun countOfFollower(
            @PathVariable(SubscribeParam.MEMBER_ID) memberId: UUID
        ): ResponseEntity<Long> {
            val countOfFollower = subscribeQueryService.getCountOfFollower(memberId)
            return ResponseEntity.ok(countOfFollower)
        }

        @PostMapping(SubscribeUrl.SUBSCRIBE)
        fun subscribe(
            @RequestBody @Valid createSubscribe: CreateSubscribe
        ): ResponseEntity<String> {
            subscribeCommandService.createSubscribe(createSubscribe)
            logger().info(
                SubscribeControllerLog.SUBSCRIBE_SUCCESS + createSubscribe.followerId +
                    SubscribeControllerLog.FOLLOWEE_INSERT_LOG + createSubscribe.followeeId
            )

            return SubscribeResponse.subscribeSuccess()
        }

        @DeleteMapping(SubscribeUrl.UNSUBSCRIBE)
        fun unsubscribe(
            @RequestBody @Valid unsubscribeRequest: UnsubscribeRequest
        ): ResponseEntity<String> {
            subscribeCommandService.unsubscribe(unsubscribeRequest)
            logger().info(
                SubscribeControllerLog.UNSUBSCRIBE_SUCCESS + unsubscribeRequest.followerId +
                    SubscribeControllerLog.FOLLOWEE_INSERT_LOG + unsubscribeRequest.followeeId
            )

            return SubscribeResponse.unsubscribeSuccess()
        }
    }
