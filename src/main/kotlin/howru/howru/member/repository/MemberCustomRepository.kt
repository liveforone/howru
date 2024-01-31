package howru.howru.member.repository

import howru.howru.member.domain.Member
import howru.howru.member.dto.response.MemberInfo
import java.util.*

interface MemberCustomRepository {
    fun findIdByEmailNullableForValidate(email: String): Long?
    fun findOneByEmail(email: String): Member
    fun findOneById(id: UUID): Member
    fun findOneDtoById(id: UUID): MemberInfo
}