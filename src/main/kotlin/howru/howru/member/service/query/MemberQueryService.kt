package howru.howru.member.service.query

import howru.howru.globalConfig.cache.constant.CacheName
import howru.howru.member.repository.MemberRepository
import howru.howru.member.cache.MemberCache
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class MemberQueryService @Autowired constructor(
    private val memberRepository: MemberRepository
) {
    @Cacheable(cacheNames = [CacheName.MEMBER], key = MemberCache.KEY)
    fun getMemberByUUID(uuid: UUID) = memberRepository.findOneDtoByUUID(uuid)
}