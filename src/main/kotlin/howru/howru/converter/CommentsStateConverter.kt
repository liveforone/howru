package howru.howru.converter

import howru.howru.comments.domain.CommentsState
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class CommentsStateConverter : AttributeConverter<CommentsState, String> {
    override fun convertToDatabaseColumn(attribute: CommentsState) = attribute.name
    override fun convertToEntityAttribute(dbData: String) = CommentsState.valueOf(dbData)
}