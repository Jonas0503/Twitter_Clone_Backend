package clone.twitter.service;

import clone.twitter.dto.TweetDTO;
import clone.twitter.exceptions.EntityAlreadyExistsException;
import clone.twitter.exceptions.EntityNotFoundException;
import clone.twitter.models.Tweet;
import clone.twitter.repo.TweetRepository;
import clone.twitter.service.mapping.MapToDto;
import clone.twitter.service.mapping.MapToEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TweetService {

    private MapToEntity mapToEntity;
    private MapToDto mapToDto;
    private TweetRepository tweetRepository;

    @Autowired
    public TweetService(MapToEntity mapToEntity, MapToDto mapToDto, TweetRepository tweetRepository) {
        this.mapToEntity = mapToEntity;
        this.mapToDto = mapToDto;
        this.tweetRepository = tweetRepository;
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
        if (tweetRepository.existsById(uuid)) {
            tweetRepository.deleteById(uuid);
        }
        else {
            throw new EntityNotFoundException(uuid);
        }
    }
}
