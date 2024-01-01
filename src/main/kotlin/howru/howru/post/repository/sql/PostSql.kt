package howru.howru.post.repository.sql

object PostSql {
    const val RANDOM_POSTS_QUERY = "select new howru.howru.post.dto.response.PostInfo(p.id, w.uuid, p.content, p.postState, p.createdDatetime) from Post p join p.writer w order by RAND() limit 10"
}