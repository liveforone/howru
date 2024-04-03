package howru.howru.converter

import howru.howru.reportState.domain.MemberState
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class MemberStateConverter : AttributeConverter<MemberState, String> {
    override fun convertToDatabaseColumn(attribute: MemberState) = attribute.name

    override fun convertToEntityAttribute(dbData: String) = MemberState.valueOf(dbData)
}
