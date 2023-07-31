package howru.howru.post.repository.query

object PostQuery {
    const val RANDOM_POST = "select new howru.howru.post.dto.response.PostInfo(p.uuid, w.uuid, p.content, p.postState, p.createdDatetime) from Post p join p.writer w order by RAND() limit 10"
}