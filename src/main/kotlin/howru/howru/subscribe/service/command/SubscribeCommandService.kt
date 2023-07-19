package howru.howru.subscribe.service.command

import howru.howru.subscribe.domain.Subscribe
import howru.howru.subscribe.dto.request.CreateSubscribe
import howru.howru.subscribe.dto.request.UnsubscribeRequest
import howru.howru.subscribe.repository.SubscribeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class SubscribeCommandService @Autowired constructor(
    private val subscribeRepository: SubscribeRepository
) {
    fun createSubscribe(createSubscribe: CreateSubscribe) {
        with(createSubscribe) {
            Subscribe.create(followeeUUID!!, followerUUID!!)
                .also { subscribeRepository.save(it) }
        }
    }

    fun unsubscribe(unsubscribeRequest: UnsubscribeRequest) {
        with(unsubscribeRequest) {
            subscribeRepository.findOneById(followeeUUID!!, followerUUID!!)
                .also { subscribeRepository.delete(it) }
        }
    }
}