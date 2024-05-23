package howru.howru.advertisement.controller.constant

object AdvertisementUrl {
    const val DETAIL = "/advertisements/{id}"
    const val ALL_AD = "/advertisements"
    const val SEARCH_COMPANY = "/advertisements/search-company"
    const val EXPIRED_AD = "/advertisements/expired"
    const val RANDOM_AD = "/advertisements/random"
    const val CREATE_HALF_AD = "/advertisements/create/half"
    const val CREATE_YEAR_AD = "/advertisements/create/year"
    const val EDIT_AD = "/advertisements/{id}"
    const val REMOVE_AD = "/advertisements/{id}"
}

object AdvertisementParam {
    const val ID = "id"
    const val COMPANY = "company"
}
