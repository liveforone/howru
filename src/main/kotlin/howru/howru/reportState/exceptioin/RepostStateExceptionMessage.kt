package howru.howru.reportState.exceptioin

enum class RepostStateExceptionMessage(
    val status: Int,
    val message: String
) {
    REPORT_STATE_IS_NULL(404, "신고 상태 정보가 존재하지 않습니다. 회원 식별자 : ")
}
