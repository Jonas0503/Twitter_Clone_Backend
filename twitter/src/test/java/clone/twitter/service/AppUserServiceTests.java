package clone.twitter.service;

import clone.twitter.TestFactory.AppUserTestFactory;
import clone.twitter.dto.AppUserDTO;
import clone.twitter.models.AppUser;
import clone.twitter.repo.AppUserRepository;
import clone.twitter.repo.TweetRepository;
import clone.twitter.service.mapping.MapToDto;
import clone.twitter.service.mapping.MapToEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AppUserServiceTests {

    @Mock
    private MapToEntity mapToEntity;

    @Mock
    private MapToDto mapToDto;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private TweetRepository tweetRepository;

    @InjectMocks
    private AppUserService appUserService;

    @Test
    public void testReadAppUser() {
        // Arrange
        AppUser appUser = AppUserTestFactory.createAppUser();
        AppUserDTO expected = mapToDto.mapToAppUserDTO(appUser);
        Mockito.when(appUserRepository.findById(appUser.getId())).thenReturn(Optional.of(appUser));

        // Act
        AppUserDTO result = appUserService.readAppUser(appUser.getId());

        // Assert
        assertEquals(expected, result);
    }
}
