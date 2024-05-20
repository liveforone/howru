package howru.howru.global.util

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.NumberPath

fun <T, Q : EntityPathBase<T>> ltLastId(
    lastId: Long?,
    qEntity: Q,
    idPathExtractor: (Q) -> NumberPath<Long>
): BooleanExpression? {
    return lastId?.takeIf { it > 0 }?.let { idPathExtractor(qEntity).lt(it) }
}

fun <T, Q : EntityPathBase<T>> ltLastTimestamp(
    lastTimestamp: Int?,
    qEntity: Q,
    timestampPathExtractor: (Q) -> NumberPath<Int>
): BooleanExpression? {
    return lastTimestamp?.takeIf { it > 0 }?.let { timestampPathExtractor(qEntity).lt(it) }
}

fun <T> findLastIdOrDefault(
    foundData: List<T>,
    idExtractor: (T) -> Long
): Long {
    return foundData.lastOrNull()?.let { idExtractor(it) } ?: 0
}
