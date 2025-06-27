package clone.twitter.repo;

import clone.twitter.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, UUID> {

    @Query("SELECT t.liked FROM Tweet t WHERE t.id = :tweetID")
    List<AppUser> findLikedAppUsersByTweetID(@Param("tweetID") UUID tweetID);

    @Query("SELECT t.disliked FROM Tweet t WHERE t.id = :tweetID")
    List<AppUser> findDislikedAppUsersByTweetID(@Param("tweetID") UUID tweetID);

}
