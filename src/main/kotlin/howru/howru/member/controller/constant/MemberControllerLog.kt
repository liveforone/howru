package howru.howru.member.controller.constant

enum class MemberControllerLog(val log: String) {
    SIGNUP_SUCCESS("회원가입 성공"),
    LOGIN_SUCCESS("로그인 성공"),
    UPDATE_EMAIL_SUCCESS("이메일 변경 성공"),
    UPDATE_PW_SUCCESS("비밀번호 변경 성공"),
    LOCK_ON_SUCCESS("회원 잠금 성공"),
    LOCK_OFF_SUCCESS("회원 잠금 해제 성공"),
    LOGOUT_SUCCESS("회원 로그아웃 성공"),
    WITHDRAW_SUCCESS("회원탈퇴 성공"),
    JWT_TOKEN_REISSUE("JWT 토큰 갱신 성공")
}