package howru.howru.advertisement.repository.constant

object AdvertisementQuery {
    const val RANDOM_AD_QUERY = "select new howru.howru.advertisement.dto.response.AdvertisementInfo(a.id, a.company, a.title, a.content, a.createdDate, a.endDate) from Advertisement a order by RAND() limit 1"
    const val DELETE_EXPIRED_QUERY = "DELETE FROM Advertisement a WHERE a.endDate < :nowDate"
    const val DELETE_EXPIRED_PARAM = "nowDate"
    const val THREE_MONTH: Long = 3
}