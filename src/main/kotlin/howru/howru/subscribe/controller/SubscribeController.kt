package howru.howru.subscribe.controller

import howru.howru.logger
import howru.howru.subscribe.controller.constant.SubscribeApiDocs
import howru.howru.subscribe.controller.constant.SubscribeParam
import howru.howru.subscribe.controller.constant.SubscribeUrl
import howru.howru.subscribe.controller.response.SubscribeResponse
import howru.howru.subscribe.dto.request.CreateSubscribe
import howru.howru.subscribe.dto.request.UnsubscribeRequest
import howru.howru.subscribe.dto.response.SubscribeInfo
import howru.howru.subscribe.log.SubscribeControllerLog
import howru.howru.subscribe.service.command.SubscribeCommandService
import howru.howru.subscribe.service.query.SubscribeQueryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@Tag(name = SubscribeApiDocs.TAG_NAME)
@RestController
class SubscribeController
    @Autowired
    constructor(
        private val subscribeQueryService: SubscribeQueryService,
        private val subscribeCommandService: SubscribeCommandService
    ) {
        @GetMapping(SubscribeUrl.FOLLOWING_INFO)
        @Operation(
            summary = SubscribeApiDocs.FOLLOWING_INFO_SUMMARY,
            description = SubscribeApiDocs.FOLLOWING_INFO_DESCRIPTION
        )
        fun followingInfo(
            @PathVariable(SubscribeParam.MEMBER_ID) memberId: UUID,
            @RequestParam(SubscribeParam.LAST_TIMESTAMP, required = false) lastTimestamp: Int?
        ): ResponseEntity<List<SubscribeInfo>> {
            val subscribes = subscribeQueryService.getFollowing(memberId, lastTimestamp)
            return ResponseEntity.ok(subscribes)
        }

        @GetMapping(SubscribeUrl.FOLLOWER_INFO)
        @Operation(
            summary = SubscribeApiDocs.FOLLOWER_INFO_SUMMARY,
            description = SubscribeApiDocs.FOLLOWER_INFO_DESCRIPTION
        )
        fun followerInfo(
            @PathVariable(SubscribeParam.MEMBER_ID) memberId: UUID,
            @RequestParam(SubscribeParam.LAST_TIMESTAMP, required = false) lastTimestamp: Int?
        ): ResponseEntity<List<SubscribeInfo>> {
            val subscribes = subscribeQueryService.getFollower(memberId, lastTimestamp)
            return ResponseEntity.ok(subscribes)
        }

        @GetMapping(SubscribeUrl.COUNT_FOLLOWING)
        @Operation(summary = SubscribeApiDocs.COUNT_FOLLOWING_SUMMARY)
        fun countOfFollowing(
            @PathVariable(SubscribeParam.MEMBER_ID) memberId: UUID
        ): ResponseEntity<Long> {
            val countOfSubscribes = subscribeQueryService.getCountOfFollowing(memberId)
            return ResponseEntity.ok(countOfSubscribes)
        }

        @GetMapping(SubscribeUrl.COUNT_FOLLOWER)
        @Operation(summary = SubscribeApiDocs.COUNT_FOLLOWER_SUMMARY)
        fun countOfFollower(
            @PathVariable(SubscribeParam.MEMBER_ID) memberId: UUID
        ): ResponseEntity<Long> {
            val countOfFollower = subscribeQueryService.getCountOfFollower(memberId)
            return ResponseEntity.ok(countOfFollower)
        }

        @PostMapping(SubscribeUrl.SUBSCRIBE)
        @Operation(summary = SubscribeApiDocs.SUBSCRIBE_SUMMARY)
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
        @Operation(summary = SubscribeApiDocs.UNSUBSCRIBE_SUMMARY)
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
