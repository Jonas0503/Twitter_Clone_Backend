package clone.twitter.service;

import clone.twitter.dto.AppUserDTO;
import clone.twitter.exceptions.EntityAlreadyExistsException;
import clone.twitter.exceptions.EntityNotFoundException;
import clone.twitter.models.AppUser;
import clone.twitter.repo.AppUserRepository;
import clone.twitter.service.mapping.MapToDto;
import clone.twitter.service.mapping.MapToEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AppUserService {

    private MapToEntity mapToEntity;
    private MapToDto mapToDto;
    private AppUserRepository appUserRepository;

    @Autowired
    public AppUserService(MapToEntity mapToEntity, MapToDto mapToDto, AppUserRepository appUserRepository) {
        this.mapToEntity = mapToEntity;
        this.mapToDto = mapToDto;
        this.appUserRepository = appUserRepository;
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
}
