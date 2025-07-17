package clone.twitter.repo;

import clone.twitter.models.AppUser;
import clone.twitter.models.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, UUID> {

    @Query("SELECT au.tweets FROM AppUser au WHERE au.id = :appUserID")
    List<Tweet> findAllTweetsByAppUserId(@Param("appUserID") UUID appUserID);

    @Query("SELECT au.likedTweets FROM AppUser au WHERE au.id = :appUserID")
    List<Tweet> findLikedTweetsByAppUserId(@Param("appUserID") UUID appUserID);

    @Query("SELECT au.dislikedTweets FROM AppUser au WHERE au.id = :appUserID")
    List<Tweet> findDislikedTweetsByAppUserId(@Param("appUserID") UUID appUserID);

    @Query("SELECT t.comments FROM Tweet t WHERE t.id = :tweetID")
    List<Tweet> findFirstLevelCommentsByTweetID(@Param("tweetID") UUID tweetID);

    @Modifying
    @Query(value = "INSERT INTO likes(post_id, app_user_id) VALUES(:tweetID, :appUserID)", nativeQuery = true)
    void saveLiked(@Param("tweetID") UUID tweetID, @Param("appUserID") UUID appUserID);

    @Modifying
    @Query(value = "INSERT INTO dislikes(post_id, app_user_id) VALUES(:tweetID, :appUserID)", nativeQuery = true)
    void saveDisliked(@Param("tweetID") UUID tweetID, @Param("appUserID") UUID appUserID);

    @Query(value = "SELECT l.post_id FROM likes l WHERE l.post_id = :tweetID AND l.app_user_id = :appUserID", nativeQuery = true)
    List<UUID> alreadyLiked(@Param("tweetID") UUID tweetID, @Param("appUserID") UUID appUserID);

    @Query(value = "SELECT d.post_id FROM dislikes d WHERE d.post_id = :tweetID AND d.app_user_id = :appUserID", nativeQuery = true)
    List<UUID> alreadyDisliked(@Param("tweetID") UUID tweetID, @Param("appUserID") UUID appUserID);

    @Modifying
    @Query(value = "DELETE FROM likes l WHERE l.post_id = :tweetID AND l.app_user_id = :appUserID", nativeQuery = true)
    void deleteLiked(@Param("tweetID") UUID tweetID, @Param("appUserID") UUID appUserID);

    @Modifying
    @Query(value = "DELETE FROM dislikes d WHERE d.post_id = :tweetID AND d.app_user_id = :appUserID", nativeQuery = true)
    void deleteDisliked(@Param("tweetID") UUID tweetID, @Param("appUserID") UUID appUserID);
}
