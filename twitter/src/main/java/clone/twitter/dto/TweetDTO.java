package clone.twitter.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
public class TweetDTO {
    private UUID id;

    private String text;

    private Instant createdAt;

    private UUID creatorID;

    private List<UUID> likedUserIDs;

    private List<UUID> dislikedUserIDs;

    private UUID parentTweetID;

    private List<UUID> commentIDs;
}
