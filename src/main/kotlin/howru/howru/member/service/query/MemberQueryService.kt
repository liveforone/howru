package howru.howru.member.service.query

import howru.howru.globalConfig.cache.constant.CacheName
import howru.howru.member.cache.MemberCache
import howru.howru.member.repository.MemberQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class MemberQueryService @Autowired constructor(
    private val memberQuery: MemberQuery
) {
    @Cacheable(cacheNames = [CacheName.MEMBER], key = MemberCache.KEY)
    fun getMemberById(id: UUID) = memberQuery.findOneDtoById(id)
}