package clone.twitter.service.mapping;

import clone.twitter.dto.AppUserDTO;
import clone.twitter.dto.TweetDTO;
import clone.twitter.models.AppUser;
import clone.twitter.models.Tweet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MapToDto {

    public AppUserDTO mapToAppUserDTO(AppUser appUser) {
        AppUserDTO appUserDTO = new AppUserDTO();

        appUserDTO.setId(appUser.getId());
        appUserDTO.setImgPath(appUser.getImgPath());
        appUserDTO.setUsername(appUser.getUsername());
        appUserDTO.setCreatedTweets(createTweetDTOList(appUser.getTweets()));
        appUserDTO.setLikedTweets(createTweetDTOList(appUser.getLikedTweets()));
        appUserDTO.setDislikedTweets(createTweetDTOList(appUser.getDislikedTweets()));

        return appUserDTO;
    }

    public TweetDTO mapToTweetDTO(Tweet tweet) {
        TweetDTO tweetDTO = new TweetDTO();

        tweetDTO.setId(tweet.getId());
        tweetDTO.setText(tweet.getText());
        tweetDTO.setCreator(mapToAppUserDTO(tweet.getAppUser()));
        tweetDTO.setLikedUserDTOs(createAppUserDTOList(tweet.getLiked()));
        tweetDTO.setDislikedUserDTOs(createAppUserDTOList(tweet.getDisliked()));

        if (tweet.getParent() == null) {
            tweetDTO.setParentTweet(null);
        }
        else {
            tweetDTO.setParentTweet(mapToTweetDTO(tweet.getParent()));
        }

        List<TweetDTO> tweetDTOList = new ArrayList<>();
        for (Tweet comment : tweet.getComments()) {
            tweetDTOList.add(mapToTweetDTO(comment));
        }
        tweetDTO.setComments(tweetDTOList);

        return tweetDTO;
    }

    public List<AppUserDTO> createAppUserDTOList(List<AppUser> appUserList) {
        List<AppUserDTO> appUserDTOList = new ArrayList<>();

        for (AppUser appUser : appUserList) {
            appUserDTOList.add(mapToAppUserDTO(appUser));
        }

        return appUserDTOList;
    }

    public List<TweetDTO> createTweetDTOList(List<Tweet> tweetList) {
        List<TweetDTO> tweetDTOList = new ArrayList<>();

        for (Tweet tweet : tweetList) {
            tweetDTOList.add(mapToTweetDTO(tweet));
        }

        return tweetDTOList;
    }
}
