package howru.howru.reply.domain

import howru.howru.comments.domain.Comments
import howru.howru.converter.ReplyStateConverter
import howru.howru.global.util.getDatetimeDigit
import howru.howru.member.domain.Member
import howru.howru.reply.domain.constant.ReplyConstant
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDateTime

@Entity
class Reply private constructor(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @ManyToOne(
        fetch = FetchType.LAZY
    ) @JoinColumn(updatable = false) @OnDelete(action = OnDeleteAction.CASCADE) val writer: Member,
    @ManyToOne(
        fetch = FetchType.LAZY
    ) @JoinColumn(updatable = false) @OnDelete(action = OnDeleteAction.CASCADE) val comment: Comments,
    @Column(nullable = false, columnDefinition = ReplyConstant.CONTENT_TYPE) var content: String,
    @Convert(converter = ReplyStateConverter::class) @Column(
        nullable = false,
        columnDefinition = ReplyConstant.REPLY_STATE_TYPE
    ) var replyState: ReplyState = ReplyState.ORIGINAL,
    @Column(
        nullable = false,
        updatable = false
    ) val createdDatetime: Long = getDatetimeDigit(LocalDateTime.now())
) {
    companion object {
        fun create(
            writer: Member,
            comment: Comments,
            content: String
        ) = Reply(writer = writer, comment = comment, content = content)
    }

    fun editContent(content: String) {
        this.content = content
        replyState = ReplyState.EDITED
    }
}
