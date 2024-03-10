package howru.howru.subscribe.repository

import howru.howru.exception.exception.SubscribeException
import howru.howru.exception.message.SubscribeExceptionMessage
import howru.howru.subscribe.domain.Subscribe
import howru.howru.subscribe.dto.response.SubscribeInfo
import howru.howru.subscribe.repository.constant.SubscribeRepoConstant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.util.*

@Component
class SubscribeQuery @Autowired constructor(
    private val subscribeRepository: SubscribeRepository
) {
    fun findOneById(followeeId: UUID, followerId: UUID): Subscribe {
        return subscribeRepository.findAll {
            select(entity(Subscribe::class))
                .from(entity(Subscribe::class))
                .where(path(Subscribe::followeeId).eq(followeeId).and(path(Subscribe::followerId).eq(followerId)))
        }.firstOrNull() ?: throw SubscribeException(SubscribeExceptionMessage.SUBSCRIBE_IS_NULL, followerId)
    }

    fun findSubscribesByFollower(followerId: UUID, page: Int): List<SubscribeInfo> {
        val pageable = PageRequest.of(page, SubscribeRepoConstant.LIMIT_PAGE)
        return subscribeRepository.findAll(pageable) {
            selectNew<SubscribeInfo>(
                path(Subscribe::followeeId),
                path(Subscribe::followerId),
            ).from(entity(Subscribe::class))
                .where(path(Subscribe::followerId).eq(followerId))
                .orderBy(path(Subscribe::timestamp).desc())
        }.filterNotNull()
    }

    fun findSubscribesByFollowee(followeeId: UUID, page: Int): List<SubscribeInfo> {
        val pageable = PageRequest.of(page, SubscribeRepoConstant.LIMIT_PAGE)
        return subscribeRepository.findAll(pageable) {
            selectNew<SubscribeInfo>(
                path(Subscribe::followeeId),
                path(Subscribe::followerId),
            ).from(entity(Subscribe::class))
                .where(path(Subscribe::followeeId).eq(followeeId))
                .orderBy(path(Subscribe::timestamp).desc())
        }.filterNotNull()
    }

    fun findFollowees(followerId: UUID): List<UUID> {
        return subscribeRepository.findAll {
            select(path(Subscribe::followeeId))
                .from(entity(Subscribe::class))
                .where(path(Subscribe::followerId).eq(followerId))
                .orderBy(path(Subscribe::timestamp).desc())
        }.filterNotNull()
    }
}