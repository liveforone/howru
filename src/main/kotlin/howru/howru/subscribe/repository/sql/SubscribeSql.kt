package howru.howru.subscribe.repository.sql

object SubscribeSql {
    const val IS_FOLLOWEE = "SELECT CASE WHEN EXISTS (" +
            "SELECT 1 FROM Subscribe s " +
            "WHERE s.followeeId = :followeeId AND s.followerId = :followerId" +
            ") THEN true ELSE false END"

    const val IS_FOLLOW_EACH = "SELECT CASE WHEN EXISTS (" +
            "SELECT 1 FROM Subscribe s1 " +
            "WHERE s1.followeeId = :followeeId AND s1.followerId = :followerId" +
            ") AND EXISTS (" +
            "SELECT 1 FROM Subscribe s2 " +
            "WHERE s2.followeeId = :followerId AND s2.followerId = :followeeId" +
            ") THEN true ELSE false END"

    const val FOLLOWEE_ID = "followeeId"
    const val FOLLOWER_ID = "followerId"
}