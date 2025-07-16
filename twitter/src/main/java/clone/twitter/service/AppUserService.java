package clone.twitter.service;

import clone.twitter.dto.AppUserDTO;
import clone.twitter.dto.TweetDTO;
import clone.twitter.exceptions.EntityAlreadyExistsException;
import clone.twitter.exceptions.EntityNotFoundException;
import clone.twitter.models.AppUser;
import clone.twitter.models.Tweet;
import clone.twitter.models.TweetStatusForAppUser;
import clone.twitter.repo.AppUserRepository;
import clone.twitter.repo.TweetRepository;
import clone.twitter.service.mapping.MapToDto;
import clone.twitter.service.mapping.MapToEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppUserService {

    private MapToEntity mapToEntity;
    private MapToDto mapToDto;
    private AppUserRepository appUserRepository;
    private TweetRepository tweetRepository;

    @Autowired
    public AppUserService(MapToEntity mapToEntity, MapToDto mapToDto, AppUserRepository appUserRepository, TweetRepository tweetRepository) {
        this.mapToEntity = mapToEntity;
        this.mapToDto = mapToDto;
        this.appUserRepository = appUserRepository;
        this.tweetRepository = tweetRepository;
    }

    @Transactional
    public AppUserDTO createAppUser(AppUserDTO appUserDTO) {
        if (appUserRepository.existsById(appUserDTO.getId())) {
            throw new EntityAlreadyExistsException(appUserDTO.getId());
        }
        else {
            AppUser appUserToCreate = mapToEntity.mapToAppUserEntity(appUserDTO);
            appUserToCreate.setId(null);
            AppUser createdAppUser = appUserRepository.save(appUserToCreate);

            return mapToDto.mapToAppUserDTO(createdAppUser);
        }
    }

    @Transactional
    public AppUserDTO readAppUser(UUID uuid) {
        Optional<AppUser> appUserOptional = appUserRepository.findById(uuid);

        if (appUserOptional.isEmpty()) {
            throw new EntityNotFoundException(uuid);
        }
        else {
            AppUser appUser = appUserOptional.get();
            return mapToDto.mapToAppUserDTO(appUser);
        }
    }

    @Transactional
    public AppUserDTO updateAppUser(AppUserDTO appUserDTO) {
        if (appUserRepository.existsById(appUserDTO.getId())) {
            AppUser appUser = mapToEntity.mapToAppUserEntity(appUserDTO);
            AppUser updatedAppUser = appUserRepository.save(appUser);

            return mapToDto.mapToAppUserDTO(updatedAppUser);
        }
        else {
            throw new EntityNotFoundException(appUserDTO.getId());
        }
    }

    @Transactional
    public void deleteAppUser(UUID uuid) {
        if (appUserRepository.existsById(uuid)) {
            appUserRepository.deleteById(uuid);
        }
        else {
            throw new EntityNotFoundException(uuid);
        }
    }

    @Transactional
    public List<TweetDTO> getCreatedTweetsByAppUser(UUID id, TweetStatusForAppUser status) {
        if (!status.equals(TweetStatusForAppUser.CREATED)) {
            throw new RuntimeException("Wrong status was used to get the created tweets.");
        }

        if (!appUserRepository.existsById(id)) {
            throw new EntityNotFoundException(id);
        }

        List<Tweet> createdTweets = tweetRepository.findAllTweetsByAppUserId(id);

        return mapToDto.mapToTweetDTOList(createdTweets);
    }

    @Transactional
    public List<TweetDTO> getLikedTweetsByAppUser(UUID id, TweetStatusForAppUser status) {
        if (!status.equals(TweetStatusForAppUser.LIKED)) {
            throw new RuntimeException("Wrong status was used to get the liked tweets.");
        }

        if (!appUserRepository.existsById(id)) {
            throw new EntityNotFoundException(id);
        }

        List<Tweet> createdTweets = tweetRepository.findLikedTweetsByAppUserId(id);

        return mapToDto.mapToTweetDTOList(createdTweets);
    }

    @Transactional
    public List<TweetDTO> getDislikedTweetsByAppUser(UUID id, TweetStatusForAppUser status) {
        if (!status.equals(TweetStatusForAppUser.DISLIKED)) {
            throw new RuntimeException("Wrong status was used to get the disliked tweets.");
        }

        if (!appUserRepository.existsById(id)) {
            throw new EntityNotFoundException(id);
        }

        List<Tweet> createdTweets = tweetRepository.findDislikedTweetsByAppUserId(id);

        return mapToDto.mapToTweetDTOList(createdTweets);
    }
}
