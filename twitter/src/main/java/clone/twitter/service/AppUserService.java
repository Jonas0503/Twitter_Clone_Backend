package clone.twitter.service;

import clone.twitter.dto.AppUserDTO;
import clone.twitter.models.AppUser;
import clone.twitter.repo.AppUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppUserService {

    private PostService postService;
    private AppUserRepository appUserRepository;

    @Autowired
    public AppUserService(PostService postService, AppUserRepository appUserRepository) {
        this.postService = postService;
        this.appUserRepository = appUserRepository;
    }

    public AppUserDTO mapToAppUserDTO(AppUser appUser) {
        AppUserDTO appUserDTO = new AppUserDTO();

        appUserDTO.setId(appUser.getId());
        appUserDTO.setImgPath(appUser.getImgPath());
        appUserDTO.setUsername(appUser.getUsername());
        appUserDTO.setPosts(postService.createPostDTOList(appUser.getPosts()));
        appUserDTO.setLikedPosts(postService.createPostDTOList(appUser.getLikedPosts()));
        appUserDTO.setDislikedPosts(postService.createPostDTOList(appUser.getDislikedPosts()));

        return appUserDTO;
    }

    public AppUser mapToAppUserEntity(AppUserDTO appUserDTO) {
        AppUser appUser = new AppUser();

        appUser.setId(appUserDTO.getId());
        appUser.setImgPath(appUserDTO.getImgPath());
        appUser.setUsername(appUserDTO.getUsername());
        appUser.setPosts(postService.createPostEntityList(appUserDTO.getPosts()));
        appUser.setLikedPosts(postService.createPostEntityList(appUserDTO.getLikedPosts()));
        appUser.setDislikedPosts(postService.createPostEntityList(appUserDTO.getDislikedPosts()));

        return appUser;
    }

    public List<AppUserDTO> createAppUserDTOList(List<AppUser> appUserList) {
        List<AppUserDTO> appUserDTOList = new ArrayList<>();

        for (AppUser appUser : appUserList) {
            appUserDTOList.add(mapToAppUserDTO(appUser));
        }

        return appUserDTOList;
    }

    public List<AppUser> createAppUserEntityList(List<AppUserDTO> appUserDTOList) {
        List<AppUser> appUserList = new ArrayList<>();

        for (AppUserDTO appUserDTO : appUserDTOList) {
            appUserList.add(mapToAppUserEntity(appUserDTO));
        }

        return appUserList;
    }

    // TODO: Exception Handling
    @Transactional
    public AppUserDTO createAppUser(AppUserDTO appUserDTO) {
        if (appUserRepository.existsById(appUserDTO.getId())) {
            throw new RuntimeException("AppUser already exists!");
        }
        else {
            AppUser appUserToCreate = mapToAppUserEntity(appUserDTO);
            AppUser createdAppUser = appUserRepository.save(appUserToCreate);

            return mapToAppUserDTO(createdAppUser);
        }
    }

    @Transactional
    public AppUserDTO readAppUser(UUID uuid) {
        Optional<AppUser> appUserOptional = appUserRepository.findById(uuid);

        if (appUserOptional.isEmpty()) {
            throw new RuntimeException("Exception Handling not yet implemented! AppUser does not exist!");
        }
        else {
            AppUser appUser = appUserOptional.get();
            return mapToAppUserDTO(appUser);
        }
    }

    @Transactional
    public AppUserDTO updateAppUser(AppUserDTO appUserDTO) {
        if (appUserRepository.existsById(appUserDTO.getId())) {
            AppUser appUser = mapToAppUserEntity(appUserDTO);
            AppUser updatedAppUser = appUserRepository.save(appUser);

            return mapToAppUserDTO(updatedAppUser);
        }
        else {
            throw new RuntimeException("AppUser does not exist!");
        }
    }

    @Transactional
    public boolean deleteAppUser(UUID uuid) {
        if (appUserRepository.existsById(uuid)) {
            appUserRepository.deleteById(uuid);
            return true;
        }
        else {
            return false;
        }
    }
}
