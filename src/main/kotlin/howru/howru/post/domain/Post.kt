package howru.howru.post.domain

import howru.howru.converter.PostStateConverter
import howru.howru.globalUtil.DATE_TYPE
import howru.howru.globalUtil.UUID_TYPE
import howru.howru.globalUtil.createUUID
import howru.howru.globalUtil.datetimeConvertToDigit
import howru.howru.member.domain.Member
import howru.howru.post.domain.constant.PostConstant
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*

@Entity
class Post private constructor(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @Column(columnDefinition = UUID_TYPE, unique = true, nullable = false) val uuid: UUID = createUUID(),
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(updatable = false) @OnDelete(action = OnDeleteAction.CASCADE) val writer: Member,
    @Column(nullable = false, columnDefinition = PostConstant.CONTENT_TYPE) var content: String,
    @Convert(converter = PostStateConverter::class) @Column(nullable = false) var postState: PostState = PostState.ORIGINAL,
    @Column(nullable = false, updatable = false, columnDefinition = DATE_TYPE) val createdDate: Long = datetimeConvertToDigit()
) {
    companion object {
        fun create(writer: Member, content: String) = Post(writer = writer, content = content)
    }

    fun editContent(updatedContent: String) {
        content = updatedContent
        postState = PostState.EDITED
    }
}