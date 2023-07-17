package howru.howru.member.dto.response

import howru.howru.member.domain.Role
import java.util.*

data class MemberInfo(
    val uuid: UUID,
    val auth: Role,
    val email: String,
    val reportCount: Long
)