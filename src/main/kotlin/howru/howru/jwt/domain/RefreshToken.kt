package howru.howru.jwt.domain

import java.util.UUID

class RefreshToken private constructor(
    val id: UUID,
    var refreshToken: String
) {
    companion object {
        fun create(id: UUID, refreshToken: String) = RefreshToken(id, refreshToken)
    }

    fun reissueRefreshToken(reissuedRefreshToken: String) {
        this.refreshToken = reissuedRefreshToken
    }
}