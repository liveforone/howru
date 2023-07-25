package howru.howru.converter

import howru.howru.post.domain.PostState
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class PostStateConverter : AttributeConverter<PostState, String> {
    override fun convertToDatabaseColumn(attribute: PostState) = attribute.name

    override fun convertToEntityAttribute(dbData: String) = PostState.valueOf(dbData)
}