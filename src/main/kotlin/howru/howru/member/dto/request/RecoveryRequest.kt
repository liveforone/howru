package howru.howru.member.dto.request

import jakarta.validation.constraints.NotBlank

data class RecoveryRequest(
    @field:NotBlank(message = "이메일을 입력하세요.") val email: String?,
    @field:NotBlank(message = "비밀번호를 입력하세요.") val pw: String?
)
