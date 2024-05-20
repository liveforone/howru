package howru.howru.member.domain.vo

import howru.howru.member.domain.MemberLock
import howru.howru.member.domain.Role
import java.util.*

data class MemberInfo(
    val id: UUID,
    val auth: Role,
    val email: String,
    val nickName: String,
    val memberLock: MemberLock
) {
    fun isUnlock(): Boolean {
        return memberLock == MemberLock.OFF
    }
}
