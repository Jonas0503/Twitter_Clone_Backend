package clone.twitter.repo;

import clone.twitter.models.Tweet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TweetRepository extends CrudRepository<Tweet, UUID> {
}
