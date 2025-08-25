package clone.twitter.controller;

import clone.twitter.dto.AppUserDTO;
import clone.twitter.dto.TweetDTO;
import clone.twitter.models.TweetStatusForAppUser;
import clone.twitter.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class AppUserController {

    private AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<AppUserDTO> getAppUser(@PathVariable UUID id) {
        AppUserDTO appUserDTO = appUserService.readAppUser(id);
        return ResponseEntity.ok(appUserDTO);
    }

    @PostMapping("/user")
    public ResponseEntity<AppUserDTO> createNewUser(@RequestBody AppUserDTO appUserDTO) {
        AppUserDTO createdAppUserDTO = appUserService.createAppUser(appUserDTO);
        return ResponseEntity.created(URI.create("/user/" + createdAppUserDTO.getId())).body(createdAppUserDTO);
    }

    @PutMapping("/user")
    public ResponseEntity<AppUserDTO> updateUser(@RequestBody AppUserDTO appUserDTO) {
        AppUserDTO updatedUser = appUserService.updateAppUser(appUserDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        appUserService.deleteAppUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{id}/tweet")
    public ResponseEntity<List<TweetDTO>> getTweetsFromAppUser(@PathVariable UUID id, @RequestParam(name = "status") TweetStatusForAppUser status) {

        List<TweetDTO> tweetDTOList = switch (status) {
            case CREATED -> appUserService.getCreatedTweetsByAppUser(id, status);
            case LIKED -> appUserService.getLikedTweetsByAppUser(id, status);
            case DISLIKED -> appUserService.getDislikedTweetsByAppUser(id, status);
        };

        return ResponseEntity.ok(tweetDTOList);
    }
}
