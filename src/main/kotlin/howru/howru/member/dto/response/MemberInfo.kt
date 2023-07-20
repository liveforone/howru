package howru.howru.member.dto.response

import howru.howru.member.domain.MemberLock
import howru.howru.member.domain.Role
import java.util.*

data class MemberInfo(
    val uuid: UUID,
    val auth: Role,
    val email: String,
    val memberLock: MemberLock,
    val reportCount: Long
) {
    fun isUnlock(): Boolean {
        return memberLock == MemberLock.OFF
    }
}