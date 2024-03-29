package howru.howru.member.controller.response

import howru.howru.member.dto.response.MemberInfo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object MemberResponse {
    private const val SIGNUP_SUCCESS = "회원가입에 성공하였습니다.\n반갑습니다."
    private const val LOGIN_SUCCESS = "로그인에 성공하였습니다.\n환영합니다."
    private const val UPDATE_PW_SUCCESS = "비밀번호를 성공적으로 변경하였습니다."
    private const val LOCK_ON_SUCCESS = "프로필 잠금에 성공하였습니다."
    private const val LOCK_OFF_SUCCESS = "프로필 잠금 해제에 성공하였습니다."
    private const val LOGOUT_SUCCESS = "로그아웃에 성공하였습니다."
    private const val RECOVERY_SUCCESS = "계정 복구에 성공하였습니다."
    private const val WITHDRAW_SUCCESS = "회원탈퇴를 성공적으로 마쳤습니다.\n안녕히가세요."

    fun infoSuccess(member: MemberInfo) = ResponseEntity.ok(member)
    fun signupSuccess() = ResponseEntity.status(HttpStatus.CREATED).body(SIGNUP_SUCCESS)
    fun loginSuccess() = ResponseEntity.ok(LOGIN_SUCCESS)
    fun updatePwSuccess() = ResponseEntity.ok(UPDATE_PW_SUCCESS)
    fun lockOnSuccess() = ResponseEntity.ok(LOCK_ON_SUCCESS)
    fun lockOffSuccess() = ResponseEntity.ok(LOCK_OFF_SUCCESS)
    fun logOutSuccess() = ResponseEntity.ok(LOGOUT_SUCCESS)
    fun recoverySuccess() = ResponseEntity.ok(RECOVERY_SUCCESS)
    fun withdrawSuccess() = ResponseEntity.ok(WITHDRAW_SUCCESS)
}