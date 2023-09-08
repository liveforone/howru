package howru.howru.advertisement.repository

import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.listQuery
import com.linecorp.kotlinjdsl.spring.data.singleQuery
import howru.howru.advertisement.domain.Advertisement
import howru.howru.advertisement.dto.response.AdvertisementInfo
import howru.howru.exception.exception.AdvertisementException
import howru.howru.exception.message.AdvertisementExceptionMessage
import howru.howru.globalUtil.getDateDigit
import jakarta.persistence.NoResultException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class AdvertisementRepositoryImpl @Autowired constructor(
    private val queryFactory: SpringDataQueryFactory
) : AdvertisementCustomRepository {
    override fun findOneById(id: Long): Advertisement {
        return try {
            queryFactory.singleQuery {
                select(entity(Advertisement::class))
                from(Advertisement::class)
                where(col(Advertisement::id).equal(id))
            }
        } catch (e: NoResultException) {
            throw AdvertisementException(AdvertisementExceptionMessage.AD_IS_NULL, id)
        }
    }

    override fun findOneDtoById(id: Long): AdvertisementInfo {
        return try {
            queryFactory.singleQuery {
                select(listOf(
                    col(Advertisement::id),
                    col(Advertisement::company),
                    col(Advertisement::title),
                    col(Advertisement::content),
                    col(Advertisement::createdDate),
                    col(Advertisement::endDate)
                ))
                from(Advertisement::class)
                where(col(Advertisement::id).equal(id))
            }
        } catch (e: NoResultException) {
            throw AdvertisementException(AdvertisementExceptionMessage.AD_IS_NULL, id)
        }
    }

    override fun findAllAdvertisement(): List<AdvertisementInfo> {
        return queryFactory.listQuery {
            select(listOf(
                col(Advertisement::id),
                col(Advertisement::company),
                col(Advertisement::title),
                col(Advertisement::content),
                col(Advertisement::createdDate),
                col(Advertisement::endDate)
            ))
            from(Advertisement::class)
            orderBy(col(Advertisement::id).desc())
        }
    }

    override fun searchAdByCompany(company: String): List<AdvertisementInfo> {
        return queryFactory.listQuery {
            select(listOf(
                col(Advertisement::id),
                col(Advertisement::company),
                col(Advertisement::title),
                col(Advertisement::content),
                col(Advertisement::createdDate),
                col(Advertisement::endDate)
            ))
            from(Advertisement::class)
            where(col(Advertisement::company).like("$company%"))
            orderBy(col(Advertisement::id).desc())
        }
    }

    override fun findExpiredAd(): List<AdvertisementInfo> {
        return queryFactory.listQuery {
            select(listOf(
                col(Advertisement::id),
                col(Advertisement::company),
                col(Advertisement::title),
                col(Advertisement::content),
                col(Advertisement::createdDate),
                col(Advertisement::endDate)
            ))
            from(Advertisement::class)
            where(col(Advertisement::endDate).lessThan(getDateDigit(LocalDate.now())))
            orderBy(col(Advertisement::id).desc())
        }
    }
}