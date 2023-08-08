package howru.howru.member.service.validator

import howru.howru.exception.exception.MemberException
import howru.howru.exception.message.MemberExceptionMessage
import howru.howru.member.repository.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MemberServiceValidator @Autowired constructor(
    private val memberRepository: MemberRepository
) {
    fun validateDuplicateEmail(email: String) {
        checkNotNull(memberRepository.findIdByEmailNullableForValidate(email)) {
            throw MemberException(MemberExceptionMessage.DUPLICATE_EMAIL)
        }
    }
}