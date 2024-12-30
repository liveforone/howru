package howru.howru.member.controller.constant

object MemberUrl {
    const val SIGNUP = "/members/signup"
    const val LOGIN = "/members/login"
    const val INFO = "/members/info"
    const val JWT_TOKEN_REISSUE = "/auth/reissue"
    const val UPDATE_PASSWORD = "/members/update/password"
    const val LOCK_ON = "/members/lock-on"
    const val LOCK_OFF = "/members/lock-off"
    const val LOGOUT = "/members/logout"
    const val RECOVERY_MEMBER = "/members/recovery"
    const val WITHDRAW = "/members/withdraw"
}

object MemberRequestHeader {
    const val ID = "id"
    const val ACCESS_TOKEN = "access-token"
    const val REFRESH_TOKEN = "refresh-token"
    const val MEMBER_ID = "member-id"
}

object MemberApiDocs {
    const val TAG_NAME = "Member"
    const val INFO_SUMMARY = "회원 정보 조회"
    const val SIGNUP_SUMMARY = "회원가입"
    const val LOGIN_SUMMARY = "로그인"
    const val LOGIN_DESCRIPTION = "로그인에 성공하면 헤더에 access, refresh 토큰과 회원의 ID를 삽입하여 리턴합니다."
    const val JWT_REISSUE_SUMMARY = "JWT 토큰 재발급"
    const val JWT_REISSUE_DESCRIPTION = "헤더에 회원의 ID와 refresh 토큰을 삽입해야합니다."
    const val UPDATE_PW_SUMMARY = "비밀번호 변경"
    const val LOCK_ON_SUMMARY = "계정 잠금"
    const val LOCK_OFF_SUMMARY = "계정 잠금 해제"
    const val LOGOUT_SUMMARY = "로그아웃"
    const val LOGOUT_DESCRIPTION = "세션이 아닌 JWT 방식을 쓰고 있기때문에 Refresh 토큰을 삭제하여 로그아웃합니다."
    const val RECOVERY_SUMMARY = "회원 복구"
    const val WITHDRAW_SUMMARY = "회원 탈퇴"
}
