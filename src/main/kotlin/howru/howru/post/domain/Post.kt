package howru.howru.post.domain

import howru.howru.globalUtil.UUID_TYPE
import howru.howru.globalUtil.createUUID
import howru.howru.globalUtil.dateConvertToInt
import howru.howru.member.domain.Member
import howru.howru.post.domain.constant.PostConstant
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDateTime
import java.util.*

@Entity
class Post private constructor(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @Column(columnDefinition = UUID_TYPE, unique = true, nullable = false) val uuid: UUID = createUUID(),
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(updatable = false) @OnDelete(action = OnDeleteAction.CASCADE) val writer: Member,
    @Column(nullable = false, columnDefinition = PostConstant.CONTENT_TYPE) var content: String,
    @Column(nullable = false, updatable = false, columnDefinition = PostConstant.DATE_TYPE) val createdDate: Long = dateConvertToInt()
) {
    companion object {
        fun create(writer: Member, content: String) = Post(writer = writer, content = content)
    }

    fun editContent(updatedContent: String) {
        content = updatedContent
    }
}