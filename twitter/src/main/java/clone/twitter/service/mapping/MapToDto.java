package clone.twitter.service.mapping;

import clone.twitter.dto.AppUserDTO;
import clone.twitter.dto.TweetDTO;
import clone.twitter.models.AppUser;
import clone.twitter.models.Tweet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class MapToDto {

    public AppUserDTO mapToAppUserDTO(AppUser appUser) {
        AppUserDTO appUserDTO = new AppUserDTO();

        appUserDTO.setId(appUser.getId());
        appUserDTO.setImgPath(appUser.getImgPath());
        appUserDTO.setUsername(appUser.getUsername());
        appUserDTO.setCreatedTweetIDs(createTweetIDList(appUser.getTweets()));
        appUserDTO.setLikedTweetIDs(createTweetIDList(appUser.getLikedTweets()));
        appUserDTO.setDislikedTweetIDs(createTweetIDList(appUser.getDislikedTweets()));

        return appUserDTO;
    }

    public TweetDTO mapToTweetDTO(Tweet tweet) {
        TweetDTO tweetDTO = new TweetDTO();

        tweetDTO.setId(tweet.getId());
        tweetDTO.setText(tweet.getText());
        tweetDTO.setCreatorID(tweet.getAppUser().getId());
        tweetDTO.setLikedUserIDs(createAppUserIDList(tweet.getLiked()));
        tweetDTO.setDislikedUserIDs(createAppUserIDList(tweet.getDisliked()));
        tweetDTO.setParentTweetID(tweet.getParent() != null ? tweet.getParent().getId() : null);
        tweetDTO.setCommentIDs(createTweetIDList(tweet.getComments()));

        return tweetDTO;
    }

    public List<TweetDTO> mapToTweetDTOList(List<Tweet> tweetList) {
        List<TweetDTO> tweetDTOList = new ArrayList<>();

        for (Tweet tweet : tweetList) {
            tweetDTOList.add(mapToTweetDTO(tweet));
        }

        return tweetDTOList;
    }

    private List<UUID> createAppUserIDList(List<AppUser> appUserList) {
        List<UUID> appUserIDList = new ArrayList<>();

        for (AppUser appUser : appUserList) {
            appUserIDList.add(appUser.getId());
        }

        return appUserIDList;
    }

    private List<UUID> createTweetIDList(List<Tweet> tweetList) {
        List<UUID> tweetIDList = new ArrayList<>();

        for (Tweet tweet : tweetList) {
            tweetIDList.add(tweet.getId());
        }

        return tweetIDList;
    }
}
