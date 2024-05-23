package howru.howru.advertisement.controller.constant

object AdvertisementUrl {
    const val DETAIL = "/advertisement/{id}"
    const val ALL_AD = "/advertisement"
    const val SEARCH_COMPANY = "/advertisement/search-company"
    const val EXPIRED_AD = "/advertisement/expired"
    const val RANDOM_AD = "/advertisement/random"
    const val CREATE_HALF_AD = "/advertisement/create/half"
    const val CREATE_YEAR_AD = "/advertisement/create/year"
    const val EDIT_AD = "/advertisement/{id}"
    const val REMOVE_AD = "/advertisement/{id}"
}

object AdvertisementParam {
    const val ID = "id"
    const val COMPANY = "company"
}
