package howru.howru.subscribe.repository.query

object SubscribeQuery {
    const val FOLLOW_EACH = "SELECT CASE WHEN EXISTS (" +
            "SELECT 1 FROM Subscribe s1 " +
            "WHERE s1.followeeUUID = :followeeUUID AND s1.followerUUID = :followerUUID" +
            ") AND EXISTS (" +
            "SELECT 1 FROM Subscribe s2 " +
            "WHERE s2.followeeUUID = :followerUUID AND s2.followerUUID = :followeeUUID" +
            ") THEN true ELSE false END"
}