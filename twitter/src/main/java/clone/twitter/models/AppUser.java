package clone.twitter.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "app_user")
@Entity
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String imgPath;

    private String username;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tweet> tweets = new ArrayList<>();

    @ManyToMany(mappedBy = "liked", cascade = CascadeType.ALL)
    private List<Tweet> likedTweets = new ArrayList<>();

    @ManyToMany(mappedBy = "disliked", cascade = CascadeType.ALL)
    private List<Tweet> dislikedTweets = new ArrayList<>();

    @Override
    public String toString() {
        List<UUID> tweetIDs = new ArrayList<>();
        for (Tweet tweet : getTweets()) {
            tweetIDs.add(tweet.getId());
        }

        List<UUID> likedTweetIDs = new ArrayList<>();
        for (Tweet tweet : getTweets()) {
            likedTweetIDs.add(tweet.getId());
        }

        List<UUID> dislikedTweetIDs = new ArrayList<>();
        for (Tweet tweet : getTweets()) {
            dislikedTweetIDs.add(tweet.getId());
        }

        return "AppUser("
                + getId() + ", "
                + getImgPath() + ", "
                + getUsername() + ", "
                + tweetIDs + ", "
                + likedTweetIDs + ", "
                + dislikedTweetIDs +
                ")";
    }
}
