package clone.twitter.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "tweet")
@Entity
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Size(min = 1, max = 256)
    private String text;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    @ManyToMany
    @JoinTable(
            name = "likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "app_user_id")
    )
    private List<AppUser> liked = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "dislikes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "app_user_id")
    )
    private List<AppUser> disliked = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Tweet parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tweet> comments = new ArrayList<>();

    @Override
    public String toString() {
        return "Tweet("
                + getId() + ", "
                + getText() + ", "
                + getAppUser() + ", "
                + getLiked() + ", "
                + getDisliked() + ", "
                + getParent() + ", "
                + getComments() +
                ")";
    }
}
