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

object AdvertisementSwagger {
    const val TAG_NAME = "Advertisement"
    const val DETAIL_SUMMARY = "광고 상세 페이지"
    const val ALL_AD_SUMMARY = "모든 광고 조회 페이지(관리자용)"
    const val SEARCH_AD_SUMMARY = "회사명으로 광고 검색 페이지(관리자용)"
    const val EXPIRED_AD_SUMMARY = "광고 게시 기한이 만료된 광고 리스트 페이지(관리자용)"
    const val RANDOM_AD_SUMMARY = "랜덤 단건 광고 페이지"
    const val CREATE_HALF_AD_SUMMARY = "기한이 반년인 광고 생성 페이지(관리자용)"
    const val CREATE_YEAR_AD_SUMMARY = "기한이 일 년인 광고 생성 페이지(관리자용)"
    const val EDIT_AD_SUMMARY = "광고 내용 수정 페이지(관리자용)"
    const val REMOVE_AD_SUMMARY = "광고 삭제 페이지(관리자용)"
}
