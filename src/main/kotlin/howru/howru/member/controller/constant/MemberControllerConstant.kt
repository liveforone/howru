package howru.howru.member.controller.constant

object MemberUrl {
    const val SIGNUP = "/member/signup"
    const val LOGIN = "/member/login"
    const val INFO = "/member/info"
    const val JWT_TOKEN_REISSUE = "/auth/reissue"
    const val UPDATE_PASSWORD = "/member/update/password"
    const val LOCK_ON = "/member/lock-on"
    const val LOCK_OFF = "/member/lock-off"
    const val LOGOUT = "/member/logout"
    const val RECOVERY_MEMBER = "/member/recovery"
    const val WITHDRAW = "/member/withdraw"
    const val PROHIBITION = "/prohibition"
}

object MemberRequestHeader {
    const val ID = "id"
    const val ACCESS_TOKEN = "access-token"
    const val REFRESH_TOKEN = "refresh-token"
    const val MEMBER_ID = "member-id"
}
