package howru.howru.comments.controller.constant

object CommentsUrl {
    const val DETAIL = "/comments/{id}"
    const val COMMENTS_PAGE = "/comments"
    const val COMMENTS_OF_OTHER_MEMBER = "/comments"
    const val MY_COMMENTS = "/comments/my"
    const val CREATE_COMMENTS = "/comments"
    const val EDIT_COMMENTS = "/comments/{id}"
    const val REMOVE_COMMENTS = "/comments/{id}"
}

object CommentsParam {
    const val ID = "id"
    const val POST_ID = "post-id"
    const val MEMBER_ID = "member-id"
    const val LAST_ID = "last-id"
}

object CommentsApiDocs {
    const val TAG_NAME = "Comments"
    const val DETAIL_SUMMARY = "댓글 상세 조회"
    const val BASIC_PAGE_SUMMARY = "게시글에 속한 댓글 페이징"
    const val BASIC_PAGE_DESCRIPTION = "쿼리스트링으로 게시글 ID를 받습니다. Cursor based 페이징울 사용합니다."
    const val MEMBER_PAGE_SUMMARY = "회원에게 속한 댓글 페이징"
    const val MEMBER_PAGE_DESCRIPTION = "쿼리스트링으로 회원 ID를 받습니다. Cursor based 페이징울 사용합니다."
    const val MY_PAGE_SUMMARY = "나(조회하려는 회원)에게 속한 댓글 페이징"
    const val MY_PAGE_DESCRIPTION = "Cursor based 페이징울 사용합니다."
    const val CREATE_SUMMARY = "댓글 생성"
    const val EDIT_SUMMARY = "댓글 내용 수정"
    const val REMOVE_SUMMARY = "댓글 삭제"
}
