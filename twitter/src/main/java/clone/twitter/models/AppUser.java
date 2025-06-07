package clone.twitter.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Table(name = "app_user")
@Entity
@Data
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String imgPath;

    private String username;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @ManyToMany(mappedBy = "liked", cascade = CascadeType.ALL)
    private List<Post> likedPosts;

    @ManyToMany(mappedBy = "disliked", cascade = CascadeType.ALL)
    private List<Post> dislikedPosts;
}
