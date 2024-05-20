package howru.howru.likes.repository

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import howru.howru.likes.exception.LikesException
import howru.howru.likes.exception.LikesExceptionMessage
import howru.howru.global.util.ltLastTimestamp
import howru.howru.likes.domain.Likes
import howru.howru.likes.domain.QLikes
import howru.howru.likes.domain.vo.LikesBelongMemberInfo
import howru.howru.likes.domain.vo.LikesBelongPostInfo
import howru.howru.likes.repository.constant.LikesRepoConstant
import java.util.*

class LikesCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val likes: QLikes = QLikes.likes
) : LikesCustomRepository {
    override fun findLikesById(
        memberId: UUID,
        postId: Long
    ): Likes {
        return jpaQueryFactory.selectFrom(likes)
            .where(likes.memberId.eq(memberId).and(likes.postId.eq(postId)))
            .fetchOne() ?: throw LikesException(LikesExceptionMessage.LIKES_IS_NULL, postId)
    }

    override fun findLikesBelongMember(
        memberId: UUID,
        lastTimestamp: Int?
    ): List<LikesBelongMemberInfo> {
        return jpaQueryFactory.select(
            Projections.constructor(
                LikesBelongMemberInfo::class.java,
                likes.postId,
                likes.timestamp
            )
        )
            .from(likes)
            .where(likes.memberId.eq(memberId).and(ltLastTimestamp(lastTimestamp, likes) { it.timestamp }))
            .orderBy(likes.timestamp.desc())
            .limit(LikesRepoConstant.LIMIT_PAGE)
            .fetch()
    }

    override fun findLikesBelongPost(
        postId: Long,
        lastTimestamp: Int?
    ): List<LikesBelongPostInfo> {
        return jpaQueryFactory.select(
            Projections.constructor(
                LikesBelongPostInfo::class.java,
                likes.memberId,
                likes.timestamp
            )
        )
            .from(likes)
            .where(likes.postId.eq(postId).and(ltLastTimestamp(lastTimestamp, likes) { it.timestamp }))
            .orderBy(likes.timestamp.desc())
            .limit(LikesRepoConstant.LIMIT_PAGE)
            .fetch()
    }
}
