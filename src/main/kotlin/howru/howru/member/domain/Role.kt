package howru.howru.member.domain

enum class Role(val auth:String) {
    MEMBER("ROLE_MEMBER"),
    ADMIN("ROLE_ADMIN"),
    SUSPEND("ROLE_SUSPEND")
}