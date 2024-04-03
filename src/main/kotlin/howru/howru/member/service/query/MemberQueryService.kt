package howru.howru.member.service.query

import howru.howru.member.repository.MemberCustomRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class MemberQueryService
    @Autowired
    constructor(
        private val memberRepository: MemberCustomRepository
    ) {
        fun getMemberById(id: UUID) = memberRepository.findMemberInfoById(id)
    }
