package howru.howru.member.dto.request

import jakarta.validation.constraints.NotBlank

data class UpdatePassword(
    @field:NotBlank(message = "새 비밀번호를 입력하세요.") val newPassword: String?,
    @field:NotBlank(message = "기존 비밀번호를 입력하세요.") val oldPassword: String?
)