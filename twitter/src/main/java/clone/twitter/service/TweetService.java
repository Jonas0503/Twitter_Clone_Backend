package clone.twitter.service;

import clone.twitter.dto.AppUserDTO;
import clone.twitter.dto.TweetDTO;
import clone.twitter.exceptions.EntityAlreadyExistsException;
import clone.twitter.exceptions.EntityNotFoundException;
import clone.twitter.models.AppUser;
import clone.twitter.models.Tweet;
import clone.twitter.repo.AppUserRepository;
import clone.twitter.repo.TweetRepository;
import clone.twitter.service.mapping.MapToDto;
import clone.twitter.service.mapping.MapToEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.UUID;

@Service
public class TweetService {

    private MapToEntity mapToEntity;
    private MapToDto mapToDto;
    private TweetRepository tweetRepository;
    private AppUserRepository appUserRepository;

    @Autowired
    public TweetService(MapToEntity mapToEntity, MapToDto mapToDto, TweetRepository tweetRepository, AppUserRepository appUserRepository) {
        this.mapToEntity = mapToEntity;
        this.mapToDto = mapToDto;
        this.tweetRepository = tweetRepository;
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    public TweetDTO createTweet(TweetDTO tweetDTO) {
        if (tweetRepository.existsById(tweetDTO.getId())) {
            throw new EntityAlreadyExistsException(tweetDTO.getId());
        }
        else {
            Tweet tweet = mapToEntity.mapToTweetEntity(tweetDTO);
            tweet.setId(null);
            Tweet createdTweet = tweetRepository.save(tweet);

            return mapToDto.mapToTweetDTO(createdTweet);
        }
    }

    @Transactional
    public TweetDTO readTweet(UUID uuid) {
        Optional<Tweet> postOptional = tweetRepository.findById(uuid);

        if (postOptional.isEmpty()) {
            throw new EntityNotFoundException(uuid);
        }
        else {
            Tweet tweet = postOptional.get();
            return mapToDto.mapToTweetDTO(tweet);
        }
    }

    @Transactional
    public Page<TweetDTO> getAllTweetsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        return tweetRepository.findAll(pageable).map(tweet -> mapToDto.mapToTweetDTO(tweet));
    }

    @Transactional
    public TweetDTO updateTweet(TweetDTO tweetDTO) {
        if (tweetRepository.existsById(tweetDTO.getId())) {
            Tweet tweet = mapToEntity.mapToTweetEntity(tweetDTO);
            Tweet updatedTweet = tweetRepository.save(tweet);

            return mapToDto.mapToTweetDTO(updatedTweet);
        }
        else {
            throw new EntityNotFoundException(tweetDTO.getId());
        }
    }

    @Transactional
    public void deleteTweet(UUID uuid) {
        tweetRepository.deleteById(uuid);
    }

    @Transactional
    public void updateLikesOnTweet(UUID tweetID, UUID appUserID) {
        if (!tweetRepository.existsById(tweetID)) {
            throw new EntityNotFoundException(tweetID);
        }
        if (!appUserRepository.existsById(appUserID)) {
            throw new EntityNotFoundException(appUserID);
        }
        if (!tweetRepository.alreadyLiked(tweetID, appUserID).isEmpty()) {
            throw new RuntimeException("Already liked");
        }

        tweetRepository.deleteDisliked(tweetID, appUserID);
        tweetRepository.saveLiked(tweetID, appUserID);
    }

    @Transactional
    public void updateDislikesOnTweet(UUID tweetID, UUID appUserID) {
        if (!tweetRepository.existsById(tweetID)) {
            throw new EntityNotFoundException(tweetID);
        }
        if (!appUserRepository.existsById(appUserID)) {
            throw new EntityNotFoundException(appUserID);
        }
        if (!tweetRepository.alreadyDisliked(tweetID, appUserID).isEmpty()) {
            throw new RuntimeException("Already disliked");
        }

        tweetRepository.deleteLiked(tweetID, appUserID);
        tweetRepository.saveDisliked(tweetID, appUserID);
    }
}
