package howru.howru.member.dto

import jakarta.validation.constraints.NotBlank

data class SignupRequest(
    @field:NotBlank(message = "이메일을 입력하세요.") val email: String?,
    @field:NotBlank(message = "비밀번호를 입력하세요.") val pw: String?,
    @field:NotBlank(message = "사용할 닉네임을 입력하세요.") val nickName: String?
)
