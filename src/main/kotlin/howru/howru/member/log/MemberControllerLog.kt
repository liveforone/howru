package howru.howru.member.log

object MemberControllerLog {
    const val SIGNUP_SUCCESS = "회원가입 성공. 회원 Email : "
    const val LOGIN_SUCCESS = "로그인 성공. 회원 Email : "
    const val UPDATE_PW_SUCCESS = "비밀번호 변경 성공. 회원 ID : "
    const val LOCK_ON_SUCCESS = "회원 잠금 성공. 회원 ID : "
    const val LOCK_OFF_SUCCESS = "회원 잠금 해제 성공. 회원 ID : "
    const val LOGOUT_SUCCESS = "회원 로그아웃 성공. 회원 ID : "
    const val RECOVERY_SUCCESS = "회원 계정 복구 성공. 회원 Email : "
    const val WITHDRAW_SUCCESS = "회원탈퇴 성공. 회원 UUID : "
    const val JWT_TOKEN_REISSUE_SUCCESS = "JWT 토큰 갱신 성공. 회원 ID : "
}
