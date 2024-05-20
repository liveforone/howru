package howru.howru.advertisement.exception

class AdvertisementException(
    val advertisementExceptionMessage: AdvertisementExceptionMessage,
    val advertisementId: Long
) : RuntimeException(advertisementExceptionMessage.message)
