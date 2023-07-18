package howru.howru.globalUtil

import java.time.Instant

fun getCurrentTimestamp(): Int {
    return Instant.now().epochSecond.toInt()
}