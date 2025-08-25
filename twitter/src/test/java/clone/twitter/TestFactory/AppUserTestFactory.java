package clone.twitter.TestFactory;

import clone.twitter.dto.AppUserDTO;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class AppUserTestFactory {

    private static Faker faker = new Faker();

    public static AppUserDTO createAppUserDTO() {
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setId(UUID.randomUUID());
        appUserDTO.setImgPath(faker.file().fileName());
        appUserDTO.setUsername(faker.name().username());
        appUserDTO.setCreatedTweetIDs(List.of(UUID.randomUUID(), UUID.randomUUID()));
        appUserDTO.setLikedTweetIDs(List.of(UUID.randomUUID(), UUID.randomUUID()));
        appUserDTO.setDislikedTweetIDs(List.of(UUID.randomUUID(), UUID.randomUUID()));

        return appUserDTO;
    }
}
