package howru.howru.advertisement.dto.response

import java.util.UUID

data class AdvertisementInfo(val uuid: UUID, val company: String, val title: String, val content: String, val createdDate: Int, val endDate: Int)