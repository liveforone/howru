package howru.howru.member.repository

import howru.howru.member.domain.Member
import howru.howru.member.domain.Role
import howru.howru.member.dto.response.MemberInfo
import java.util.*

interface MemberCustomRepository {
    fun findMemberByEmail(email: String): Member
    fun findMemberById(id: UUID): Member
    fun findMemberByEmailIncludeWithdraw(email: String): Member
    fun findMemberInfoById(id: UUID): MemberInfo
    fun findAuthById(id: UUID): Role
}