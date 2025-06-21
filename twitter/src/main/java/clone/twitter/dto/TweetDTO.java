package clone.twitter.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class TweetDTO {
    private UUID id;

    private String text;

    private AppUserDTO creator;

    private List<AppUserDTO> likedUserDTOs;

    private List<AppUserDTO> dislikedUserDTOs;

    private TweetDTO parentTweet;

    private List<TweetDTO> comments;
}
