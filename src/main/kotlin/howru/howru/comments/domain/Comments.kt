package howru.howru.comments.domain

import howru.howru.comments.domain.constant.CommentsConstant
import howru.howru.converter.CommentsStateConverter
import howru.howru.global.util.getDatetimeDigit
import howru.howru.member.domain.Member
import howru.howru.post.domain.Post
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDateTime

@Entity
class Comments private constructor(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @ManyToOne(
        fetch = FetchType.LAZY
    ) @JoinColumn(updatable = false) @OnDelete(action = OnDeleteAction.CASCADE) val writer: Member,
    @ManyToOne(
        fetch = FetchType.LAZY
    ) @JoinColumn(updatable = false) @OnDelete(action = OnDeleteAction.CASCADE) val post: Post,
    @Column(nullable = false, columnDefinition = CommentsConstant.CONTENT_TYPE) var content: String,
    @Convert(converter = CommentsStateConverter::class) @Column(
        nullable = false,
        columnDefinition = CommentsConstant.COMMENT_STATE_TYPE
    ) var commentsState: CommentsState = CommentsState.ORIGINAL,
    @Column(
        nullable = false,
        updatable = false
    ) val createdDatetime: Long = getDatetimeDigit(LocalDateTime.now())
) {
    companion object {
        fun create(
            writer: Member,
            post: Post,
            content: String
        ) = Comments(writer = writer, post = post, content = content)
    }

    fun editContentAndState(content: String) {
        this.content = content
        commentsState = CommentsState.EDITED
    }
}
