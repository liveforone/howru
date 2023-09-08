package howru.howru.exception.message

enum class MemberExceptionMessage(val status: Int, val message: String) {
    SUSPEND_MEMBER(401, "정지된 계정입니다. 회원식별자 : "),
    WRONG_PASSWORD(400, "비밀번호를 틀렸습니다. 회원식별자 : "),
    MEMBER_IS_NULL(404, "회원이 존재하지 않습니다. 회원식별자 : "),
    DUPLICATE_EMAIL(400, "중복되는 이메일이 존재합니다. 회원식별자 : ")
}