package howru.howru.comments.domain

import howru.howru.globalUtil.UUID_TYPE
import howru.howru.globalUtil.createUUID
import howru.howru.member.domain.Member
import howru.howru.post.domain.Post
import howru.howru.comments.domain.constant.CommentsConstant
import howru.howru.converter.CommentsStateConverter
import howru.howru.globalUtil.DATETIME_TYPE
import howru.howru.globalUtil.getDatetimeDigit
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDateTime
import java.util.*

@Entity
class Comments private constructor(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @Column(columnDefinition = UUID_TYPE, unique = true, nullable = false) val uuid: UUID = createUUID(),
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(updatable = false) @OnDelete(action = OnDeleteAction.CASCADE) val writer: Member,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(updatable = false) @OnDelete(action = OnDeleteAction.CASCADE) val post: Post,
    @Column(nullable = false, columnDefinition = CommentsConstant.CONTENT_TYPE) var content: String,
    @Convert(converter = CommentsStateConverter::class) @Column(
        nullable = false,
        columnDefinition = CommentsConstant.COMMENT_STATE_TYPE
    ) var commentsState: CommentsState = CommentsState.ORIGINAL,
    @Column(
        nullable = false,
        updatable = false,
        columnDefinition = DATETIME_TYPE
    ) val createdDatetime: Long = getDatetimeDigit(LocalDateTime.now())
) {
    companion object {
        fun create(writer: Member, post: Post, content: String) = Comments(writer = writer, post = post, content = content)
    }

    fun editContent(content: String) {
        this.content = content
        commentsState = CommentsState.EDITED
    }
}