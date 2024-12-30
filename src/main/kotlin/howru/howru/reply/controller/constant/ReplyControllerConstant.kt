package howru.howru.reply.controller.constant

object ReplyUrl {
    const val DETAIL = "/replies/{id}"
    const val REPLY_PAGE = "replies"
    const val MY_REPLY = "/replies/my"
    const val CREATE = "/replies"
    const val EDIT = "/replies/{id}"
    const val REMOVE = "/replies/{id}"
}

object ReplyParam {
    const val ID = "id"
    const val COMMENT_ID = "comment-id"
    const val LAST_ID = "last-id"
}

object ReplyApiDocs {
    const val TAG_NAME = "Reply"
    const val DETAIL_SUMMARY = "대댓글 단건 조회"
    const val BASIC_PAGE_SUMMARY = "댓글에 속한 대댓글 페이징"
    const val BASIC_PAGE_DESCRIPTION = "쿼리스트링으로 댓글 ID를 받습니다. Cursor based 페이징울 사용합니다."
    const val MY_PAGE_SUMMARY = "나(회원)의 대댓글 페이징"
    const val MY_PAGE_DESCRIPTION = "Cursor based 페이징울 사용합니다."
    const val CREATE_SUMMARY = "대댓글 생성"
    const val EDIT_SUMMARY = "대댓글 수정"
    const val REMOVE_SUMMARY = "대댓글 삭제"
}
