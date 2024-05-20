package howru.howru.advertisement.domain.vo

data class AdvertisementInfo(
    val id: Long,
    val company: String,
    val title: String,
    val content: String,
    val createdDate: Int,
    val endDate: Int
)
