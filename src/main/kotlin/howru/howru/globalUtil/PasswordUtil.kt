package howru.howru.globalUtil

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

fun encodePassword(password: String): String = BCryptPasswordEncoder().encode(password)
fun isMatchPassword(password: String, originalPassword: String) =
    BCryptPasswordEncoder().matches(password, originalPassword)