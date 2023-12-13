package howru.howru.member.service.validator

import howru.howru.exception.exception.MemberException
import howru.howru.exception.message.MemberExceptionMessage
import howru.howru.logger
import howru.howru.member.log.MemberValidatorLog
import howru.howru.member.repository.MemberQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MemberServiceValidator @Autowired constructor(
    private val memberQuery: MemberQuery
) {
    fun validateDuplicateEmail(email: String) {
        require (memberQuery.findIdByEmailNullableForValidate(email) == null) {
            logger().warn(MemberValidatorLog.DUPLICATE_EMAIL + email)
            throw MemberException(MemberExceptionMessage.DUPLICATE_EMAIL, email)
        }
    }
}