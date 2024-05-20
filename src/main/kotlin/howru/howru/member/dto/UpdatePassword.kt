package howru.howru.member.dto

import jakarta.validation.constraints.NotBlank

data class UpdatePassword(
    @field:NotBlank(message = "새 비밀번호를 입력하세요.") var newPassword: String?,
    @field:NotBlank(message = "기존 비밀번호를 입력하세요.") var oldPassword: String?
)
