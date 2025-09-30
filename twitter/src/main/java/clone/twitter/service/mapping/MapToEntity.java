package clone.twitter.service.mapping;

import clone.twitter.dto.AppUserDTO;
import clone.twitter.dto.TweetDTO;
import clone.twitter.exceptions.EntityNotFoundException;
import clone.twitter.models.AppUser;
import clone.twitter.models.Tweet;
import clone.twitter.repo.AppUserRepository;
import clone.twitter.repo.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MapToEntity {

    private AppUserRepository appUserRepository;
    private TweetRepository tweetRepository;

    @Autowired
    public MapToEntity(AppUserRepository appUserRepository, TweetRepository tweetRepository) {
        this.appUserRepository = appUserRepository;
        this.tweetRepository = tweetRepository;
    }

    public AppUser mapToAppUserEntity(AppUserDTO appUserDTO) {
        AppUser appUser = new AppUser();

        appUser.setId(appUserDTO.getId());
        appUser.setImgPath(appUserDTO.getImgPath());
        appUser.setUsername(appUserDTO.getUsername());
        appUser.setTweets(tweetRepository.findAllTweetsByAppUserId(appUserDTO.getId()));
        appUser.setLikedTweets(tweetRepository.findLikedTweetsByAppUserId(appUserDTO.getId()));
        appUser.setDislikedTweets(tweetRepository.findDislikedTweetsByAppUserId(appUserDTO.getId()));

        return appUser;
    }

    public Tweet mapToTweetEntity(TweetDTO tweetDTO) {
        Tweet tweet = new Tweet();

        tweet.setId(tweetDTO.getId());
        tweet.setText(tweetDTO.getText());

        // NULL if a new Tweet is created
        if (tweetDTO.getCreatedAt() == null) {
            tweet.setCreatedAt(Instant.now());
        }
        else {
            tweet.setCreatedAt(tweetDTO.getCreatedAt());
        }

        Optional<AppUser> appUserOptional = appUserRepository.findById(tweetDTO.getCreatorID());
        if (appUserOptional.isEmpty()) {
            throw new EntityNotFoundException(tweetDTO.getCreatorID());
        }
        else {
            tweet.setAppUser(appUserOptional.get());
        }

        tweet.setLiked(appUserRepository.findLikedAppUsersByTweetID(tweetDTO.getId()));
        tweet.setDisliked(appUserRepository.findDislikedAppUsersByTweetID(tweetDTO.getId()));

        if (tweetDTO.getParentTweetID() == null) {
            tweet.setParent(null);
        }
        else {
            Optional<Tweet> tweetParentOptional = tweetRepository.findById(tweetDTO.getParentTweetID());
            if (tweetParentOptional.isEmpty()) {
                throw new EntityNotFoundException(tweetDTO.getParentTweetID());
            }
            else {
                tweet.setParent(tweetParentOptional.get());
            }
        }

        tweet.setComments(tweetRepository.findFirstLevelCommentsByTweetID(tweetDTO.getId()));

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
