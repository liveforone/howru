package howru.howru.reportState.domain

import howru.howru.converter.MemberStateConverter
import howru.howru.globalUtil.DATE_TYPE
import howru.howru.globalUtil.convertDateToLocalDate
import howru.howru.globalUtil.getDateDigit
import howru.howru.member.domain.Member
import howru.howru.reportState.domain.constant.ReportStateConstant
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDate

@Entity
class ReportState private constructor(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(updatable = false) @OnDelete(action = OnDeleteAction.CASCADE) val member: Member,
    @Convert(converter = MemberStateConverter::class) @Column(
        nullable = false,
        columnDefinition = ReportStateConstant.MEMBER_STATE_TYPE
    ) var memberState: MemberState = MemberState.NORMAL,
    @Column(nullable = false, columnDefinition = DATE_TYPE) var modifiedStateDate: Int = getDateDigit(LocalDate.now()),
    @Column(nullable = false) var reportCount: Int = ReportStateConstant.BASIC_REPORT
) {
    companion object {
        fun create(member: Member) = ReportState(member = member)
    }

    fun releaseSuspend() {
        val now = LocalDate.now()
        val modifiedDate = convertDateToLocalDate(modifiedStateDate)
        when (memberState) {
            MemberState.SUSPEND_MONTH -> {
                val oneMonthReleaseDate = modifiedDate.plusMonths(ReportStateConstant.ONE_MONTH)
                if (now.isAfter(oneMonthReleaseDate) || now.isEqual(oneMonthReleaseDate)) {
                    memberState = MemberState.NORMAL
                }
            }
            MemberState.SUSPEND_SIX_MONTH -> {
                val sixMonthReleaseDate = modifiedDate.plusMonths(ReportStateConstant.SIX_MONTH)
                if (now.isAfter(sixMonthReleaseDate) || now.isEqual(sixMonthReleaseDate)) {
                    memberState = MemberState.NORMAL
                }
            }
            else -> Unit
        }
    }

    fun isNotSuspend() = memberState == MemberState.NORMAL

    fun addReport() {
        reportCount += ReportStateConstant.BASIC_VARIATION

        fun updateModifiedStateDate(newState: MemberState) {
            memberState = newState
            modifiedStateDate = getDateDigit(LocalDate.now())
        }

        when {
            reportCount >= ReportStateConstant.SUSPEND_FOREVER_CNT -> updateModifiedStateDate(MemberState.SUSPEND_FOREVER)
            reportCount >= ReportStateConstant.SUSPEND_SIX_MONTH_CNT -> updateModifiedStateDate(MemberState.SUSPEND_SIX_MONTH)
            reportCount >= ReportStateConstant.SUSPEND_MONTH_CNT -> updateModifiedStateDate(MemberState.SUSPEND_MONTH)
        }
    }
}