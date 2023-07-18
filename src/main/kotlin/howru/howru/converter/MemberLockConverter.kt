package howru.howru.converter

import howru.howru.member.domain.MemberLock
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class MemberLockConverter : AttributeConverter<MemberLock, String> {
    override fun convertToDatabaseColumn(attribute: MemberLock) = attribute.name

    override fun convertToEntityAttribute(dbData: String) = MemberLock.valueOf(dbData)
}