package clone.twitter.TestFactory;

import clone.twitter.dto.TweetDTO;
import clone.twitter.models.AppUser;
import clone.twitter.models.Tweet;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Component
public class TweetTestFactory {

    private static Faker faker = new Faker();


    public static TweetDTO createTweetDTO() {
        TweetDTO tweetDTO = new TweetDTO();
        tweetDTO.setId(UUID.randomUUID());
        tweetDTO.setText(faker.lorem().characters(1, 256));
        tweetDTO.setCreatorID(UUID.randomUUID());
        tweetDTO.setLikedUserIDs(List.of(UUID.randomUUID(), UUID.randomUUID()));
        tweetDTO.setDislikedUserIDs(List.of(UUID.randomUUID(), UUID.randomUUID()));
        tweetDTO.setParentTweetID(UUID.randomUUID());
        tweetDTO.setCommentIDs(List.of(UUID.randomUUID(), UUID.randomUUID()));

        return tweetDTO;
    }


    public static Tweet createTweet() {
        Tweet tweet = createTweetWithoutParentAndComments();
        tweet.setParent(createParentTweet(tweet));
        tweet.setComments(createTweetComments(tweet, 2));

        return tweet;
    }


    private static Tweet createTweetWithoutParentAndComments() {
        // Pos 1 -> tweet
        // Pos 2 -> dislikedTweet
        List<Tweet> tweets = new ArrayList<>(2);

        // set basic and distinct data -> an user can't like and dislike the same tweet -> two different tweets are used
        for (int i = 0; i < 2; i++) {
            Tweet tweet = new Tweet();
            tweet.setId(UUID.randomUUID());
            tweet.setText(faker.lorem().characters(1, 256));
            tweets.add(tweet);
        }

        for (Tweet tweet : tweets) {
            tweet.setAppUser(createAppUserForTweet(List.of(tweets.getFirst()), List.of(tweets.getFirst()), List.of(tweets.getLast())));
            tweet.setLiked(List.of(createAppUserForTweet(List.of(tweets.getFirst()), List.of(tweets.getFirst()), List.of(tweets.getLast()))));
            tweet.setDisliked(List.of(createAppUserForTweet(List.of(tweets.getFirst()), List.of(tweets.getFirst()), List.of(tweets.getLast()))));
        }

        return tweets.getFirst();
    }


    private static Tweet createParentTweet(Tweet childTweet) {
        Tweet parentTweet = createTweetWithoutParentAndComments();
        parentTweet.setParent(null);
        parentTweet.setComments(List.of(childTweet));

        return parentTweet;
    }


    private static List<Tweet> createTweetComments(Tweet parentTweet, int numberOfComments) {
        List<Tweet> comments = new LinkedList<>();

        for (int i = 0; i < numberOfComments; i++) {
            Tweet comment = createTweetWithoutParentAndComments();
            comment.setParent(parentTweet);
            comment.setComments(List.of());
            comments.add(comment);
        }

        return comments;
    }


    private static AppUser createAppUserForTweet(List<Tweet> createdTweets, List<Tweet> likedTweets, List<Tweet> dislikedTweets) {
        AppUser appUser = new AppUser();
        appUser.setId(UUID.randomUUID());
        appUser.setImgPath(faker.file().fileName());
        appUser.setUsername(faker.name().username());
        appUser.setTweets(createdTweets);
        appUser.setLikedTweets(likedTweets);
        appUser.setDislikedTweets(dislikedTweets);

        return appUser;
    }
}
