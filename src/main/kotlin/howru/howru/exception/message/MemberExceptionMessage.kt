package howru.howru.exception.message

enum class MemberExceptionMessage(val status: Int, val message: String) {
    SUSPEND_MEMBER(401, "정지된 계정입니다. 회원식별자 : "),
    WRONG_PASSWORD(400, "비밀번호를 틀렸습니다. 회원식별자 : "),
    MEMBER_IS_NULL(404, "회원이 존재하지 않습니다. 회원식별자 : "),
    AUTH_IS_NOT_ADMIN(401, "운영자가 아니여서 광고 접근이 불가능합니다. 회원식별자 : "),
    TOKEN_REISSUE_HEADER_IS_NULL(404, "토큰 갱신 헤더가 비어있습니다.")
}
