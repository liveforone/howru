package howru.howru.reply.domain

import howru.howru.comments.domain.Comments
import howru.howru.converter.ReplyStateConverter
import howru.howru.globalUtil.DATE_TYPE
import howru.howru.globalUtil.UUID_TYPE
import howru.howru.globalUtil.createUUID
import howru.howru.globalUtil.datetimeConvertToDigit
import howru.howru.member.domain.Member
import howru.howru.reply.domain.constant.ReplyConstant
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*

@Entity
class Reply private constructor(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @Column(columnDefinition = UUID_TYPE, unique = true, nullable = false) val uuid: UUID = createUUID(),
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(updatable = false) @OnDelete(action = OnDeleteAction.CASCADE) val writer: Member,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(updatable = false) @OnDelete(action = OnDeleteAction.CASCADE) val comment: Comments,
    @Column(nullable = false, columnDefinition = ReplyConstant.CONTENT_TYPE) var content: String,
    @Convert(converter = ReplyStateConverter::class) @Column(nullable = false) var replyState: ReplyState = ReplyState.ORIGINAL,
    @Column(nullable = false, updatable = false, columnDefinition = DATE_TYPE) val createdDate: Long = datetimeConvertToDigit()
) {
    companion object {
        fun create(writer: Member, comment: Comments, content: String) = Reply(writer = writer, comment = comment, content = content)
    }

    fun editContent(content: String) {
        this.content = content
        replyState = ReplyState.EDITED
    }
}