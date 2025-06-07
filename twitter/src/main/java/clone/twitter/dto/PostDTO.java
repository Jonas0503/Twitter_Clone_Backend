package clone.twitter.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PostDTO {
    private UUID id;

    private String text;

    private AppUserDTO appUser;

    private List<AppUserDTO> liked;

    private List<AppUserDTO> disliked;

    private PostDTO parent;

    private List<PostDTO> comments;
}
