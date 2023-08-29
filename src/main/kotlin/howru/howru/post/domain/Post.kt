package howru.howru.post.domain

import howru.howru.converter.PostStateConverter
import howru.howru.globalUtil.*
import howru.howru.member.domain.Member
import howru.howru.post.domain.constant.PostConstant
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDateTime

@Entity
class Post private constructor(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(updatable = false) @OnDelete(action = OnDeleteAction.CASCADE) val writer: Member,
    @Column(nullable = false, columnDefinition = PostConstant.CONTENT_TYPE) var content: String,
    @Convert(converter = PostStateConverter::class) @Column(
        nullable = false,
        columnDefinition = PostConstant.POST_STATE_TYPE
    ) var postState: PostState = PostState.ORIGINAL,
    @Column(
        nullable = false,
        updatable = false,
        columnDefinition = DATETIME_TYPE
    ) val createdDatetime: Long = getDatetimeDigit(LocalDateTime.now())
) {
    companion object {
        fun create(writer: Member, content: String) = Post(writer = writer, content = content)
    }

    fun editContent(updatedContent: String) {
        content = updatedContent
        postState = PostState.EDITED
    }
}