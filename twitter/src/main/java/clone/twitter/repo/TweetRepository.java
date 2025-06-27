package clone.twitter.repo;

import clone.twitter.models.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
