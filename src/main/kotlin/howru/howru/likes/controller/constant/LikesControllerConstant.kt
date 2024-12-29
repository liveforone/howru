package howru.howru.likes.controller.constant

object LikesUrl {
    const val COUNT_OF_LIKES = "/likes/count"
    const val LIKES_BY_MEMBER = "/likes"
    const val LIKES_BY_POST = "/likes"
    const val LIKE = "/likes"
    const val DISLIKE = "/likes/dislike"
}

object LikesParam {
    const val MEMBER_ID = "member-id"
    const val POST_ID = "post-id"
    const val LAST_TIMESTAMP = "last-timestamp"
}

object LikesApiDocs {
    const val TAG_NAME = "Likes"
    const val COUNT_SUMMARY = "게시글의 좋아요 갯수 조회"
    const val LIKES_BY_MEMBER_SUMMARY = "회원이 좋아한 게시글 ID 페이징"
    const val LIKES_BY_MEMBER_DESCRIPTION = "쿼리 스트링으로 회원 ID를 받습니다. Cursor based 페이징울 사용합니다."
    const val LIKES_BY_POST_SUMMARY = "게시글을 좋아한 회원 ID 페이징"
    const val LIKES_BY_POST_DESCRIPTION = "쿼리 스트링으로 게시글 ID를 받습니다. Cursor based 페이징울 사용합니다."
    const val LIKE_SUMMARY = "좋아요 생성"
    const val DISLIKE_SUMMARY = "좋아요 취소"
}
