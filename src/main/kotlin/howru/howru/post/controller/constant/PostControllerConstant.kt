package howru.howru.post.controller.constant

object PostUrl {
    const val DETAIL = "/posts/{id}"
    const val ALL = "/posts"
    const val POST_OF_OTHER_MEMBER = "/posts"
    const val MY_POST = "/posts/my"
    const val POST_OF_FOLLOWEE = "/posts/followees"
    const val COUNT_OF_POST = "/posts/count"
    const val RECOMMEND = "/posts/recommend"
    const val RANDOM = "/posts/random"
    const val CREATE = "/posts"
    const val EDIT = "/posts/{id}"
    const val REMOVE = "/posts/{id}"
}

object PostParam {
    const val ID = "id"
    const val MEMBER_ID = "member-id"
    const val LAST_ID = "last-id"
    const val CONTENT = "content"
}

object PostApiDocs {
    const val TAG_NAME = "Post"
    const val DETAIL_SUMMARY = "게시글 단건 조회"
    const val BASIC_PAGE_SUMMARY = "게시글 기본 페이징"
    const val BASIC_PAGE_DESCRIPTION = "Cursor based 페이징울 사용합니다."
    const val MEMBER_PAGE_SUMMARY = "회원에게 속한 게시글 페이징"
    const val MEMBER_PAGE_DESCRIPTION = "쿼리스트링으로 회원 ID를 받습니다. Cursor based 페이징울 사용합니다."
    const val MY_PAGE_SUMMARY = "나(회원)의 게시글 페이징"
    const val MY_PAGE_DESCRIPTION = "Cursor based 페이징울 사용합니다."
    const val FOLLOWEES_PAGE_SUMMARY = "회원이 팔로우 하는 사람들의 게시글 페이징"
    const val FOLLOWEES_PAGE_DESCRIPTION = "Cursor based 페이징울 사용합니다."
    const val COUNT_OF_POST_SUMMARY = "회원의 게시글 갯수 조회"
    const val RECOMMEND_PAGE_SUMMARY = "추천 게시글 페이징"
    const val RECOMMEND_PAGE_DESCRIPTION = "문자열을 받습니다. 해당 문자열을 바탕으로 문자들을 추출하여 게시글을 추천합니다."
    const val RANDOM_SUMMARY = "랜덤 게시글"
    const val RANDOM_DESCRIPTION = "무작위 게시글 10개를 뽑아 리턴합니다."
    const val CREATE_SUMMARY = "게시글 생성"
    const val EDIT_SUMMARY = "게시글 수정"
    const val REMOVE_SUMMARY = "게시글 삭제"
}
