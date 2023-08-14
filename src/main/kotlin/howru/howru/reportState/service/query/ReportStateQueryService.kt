package howru.howru.reportState.service.query

import howru.howru.reportState.repository.RepostStateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class ReportStateQueryService @Autowired constructor(
    private val repostStateRepository: RepostStateRepository
) {
    fun getOneByMemberUUID(memberUUID: UUID) = repostStateRepository.findOneDtoByMemberUUID(memberUUID)
}