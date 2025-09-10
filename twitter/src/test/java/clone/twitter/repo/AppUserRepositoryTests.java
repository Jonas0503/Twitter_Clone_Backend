package clone.twitter.repo;

import clone.twitter.TestFactory.AppUserTestFactory;
import clone.twitter.TestFactory.TweetTestFactory;
import clone.twitter.models.AppUser;
import clone.twitter.models.Tweet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class AppUserRepositoryTests {

    @Autowired
    public AppUserRepository appUserRepository;

    @Autowired
    public TweetRepository tweetRepository;


    private void saveTweets(List<Tweet> tweets, AppUser creator, AppUser liker, AppUser disliker) {
        for (Tweet tweet : tweets) {
            tweet.setId(null);
            tweetRepository.save(tweet);
        }
    }


    @Test
    public void testSaveAppUserNoTweets() {
        // Arrange
        AppUser appUser = AppUserTestFactory.createAppUser();
        appUser.setId(null);
        appUser.setTweets(List.of());
        appUser.setLikedTweets(List.of());
        appUser.setDislikedTweets(List.of());

        // Act
        AppUser createdAppUser = appUserRepository.save(appUser);

        // Assert
        assertEquals(createdAppUser, appUser);
    }
}
