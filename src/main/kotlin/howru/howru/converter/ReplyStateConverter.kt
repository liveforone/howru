package howru.howru.converter

import howru.howru.reply.domain.ReplyState
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class ReplyStateConverter : AttributeConverter<ReplyState, String> {
    override fun convertToDatabaseColumn(attribute: ReplyState) = attribute.name

    override fun convertToEntityAttribute(dbData: String) = ReplyState.valueOf(dbData)
}