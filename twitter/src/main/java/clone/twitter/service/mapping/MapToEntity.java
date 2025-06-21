package clone.twitter.service.mapping;

import clone.twitter.dto.AppUserDTO;
import clone.twitter.dto.TweetDTO;
import clone.twitter.models.AppUser;
import clone.twitter.models.Tweet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MapToEntity {

    public AppUser mapToAppUserEntity(AppUserDTO appUserDTO) {
        AppUser appUser = new AppUser();

        appUser.setId(appUserDTO.getId());
        appUser.setImgPath(appUserDTO.getImgPath());
        appUser.setUsername(appUserDTO.getUsername());
        appUser.setTweets(createTweetEntityList(appUserDTO.getCreatedTweets()));
        appUser.setLikedTweets(createTweetEntityList(appUserDTO.getLikedTweets()));
        appUser.setDislikedTweets(createTweetEntityList(appUserDTO.getDislikedTweets()));

        return appUser;
    }

    public Tweet mapToTweetEntity(TweetDTO tweetDTO) {
        Tweet tweet = new Tweet();

        tweet.setId(tweetDTO.getId());
        tweet.setText(tweetDTO.getText());
        tweet.setAppUser(mapToAppUserEntity(tweetDTO.getCreator()));
        tweet.setLiked(createAppUserEntityList(tweetDTO.getLikedUserDTOs()));
        tweet.setDisliked(createAppUserEntityList(tweetDTO.getDislikedUserDTOs()));

        if (tweetDTO.getParentTweet() == null) {
            tweet.setParent(null);
        }
        else {
            tweet.setParent(mapToTweetEntity(tweetDTO.getParentTweet()));
        }

        List<Tweet> tweetEntityList = new ArrayList<>();
        for (TweetDTO comment : tweetDTO.getComments()) {
            tweetEntityList.add(mapToTweetEntity(comment));
        }
        tweet.setComments(tweetEntityList);

        return tweet;
    }

    public List<AppUser> createAppUserEntityList(List<AppUserDTO> appUserDTOList) {
        List<AppUser> appUserList = new ArrayList<>();

        for (AppUserDTO appUserDTO : appUserDTOList) {
            appUserList.add(mapToAppUserEntity(appUserDTO));
        }

        return appUserList;
    }

    public List<Tweet> createTweetEntityList(List<TweetDTO> tweetDTOList) {
        List<Tweet> tweetList = new ArrayList<>();

        for (TweetDTO tweetDTO : tweetDTOList) {
            tweetList.add(mapToTweetEntity(tweetDTO));
        }

        return tweetList;
    }
}
