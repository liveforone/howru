package howru.howru.exception.exception

import howru.howru.exception.message.AdvertisementExceptionMessage

class AdvertisementException(val advertisementExceptionMessage: AdvertisementExceptionMessage, val advertisementId: Long) : RuntimeException(advertisementExceptionMessage.message)