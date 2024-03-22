package howru.howru.post.repository.sql

object PostSql {
    const val RANDOM_POSTS_QUERY = "select new howru.howru.post.dto.response.PostInfo(p.id, p.writer.id, p.content, p.postState, p.createdDatetime) from Post p order by RAND() limit 10"
    const val COUNT_OF_POST_BY_WRITER_QUERY = "select count(*) from Post p where p.writer.id = :writerId"
    const val WRITER_ID = "writerId"
}