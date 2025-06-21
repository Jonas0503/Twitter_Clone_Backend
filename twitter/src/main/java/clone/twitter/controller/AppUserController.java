package clone.twitter.controller;

import clone.twitter.dto.AppUserDTO;
import clone.twitter.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

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
        UUID id = createdAppUserDTO.getId();
        return ResponseEntity.created(URI.create("/user/" + id)).body(createdAppUserDTO);
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

}
