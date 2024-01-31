package howru.howru.jwt.domain

import howru.howru.globalUtil.UUID_TYPE
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID

@Entity
class RefreshToken private constructor(
    @Id @Column(columnDefinition = UUID_TYPE) val id: UUID,
    var refreshToken: String? = null
) {
    companion object {
        fun create(id: UUID, refreshToken: String) = RefreshToken(id, refreshToken)
    }

    fun reissueRefreshToken(reissuedRefreshToken: String) {
        this.refreshToken = reissuedRefreshToken
    }

    fun clearToken() {
        this.refreshToken = null
    }
}