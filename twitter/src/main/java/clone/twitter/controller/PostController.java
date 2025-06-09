package clone.twitter.controller;

import clone.twitter.dto.PostDTO;
import clone.twitter.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

// TODO: Exception Handling
@RestController
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<PostDTO> getPost(@PathVariable UUID id) {
        PostDTO postDTO = postService.readPost(id);
        return ResponseEntity.ok(postDTO);
    }

    @PostMapping("/post")
    public ResponseEntity<PostDTO> createNewPost(@RequestBody PostDTO postDTO) {
        PostDTO createdPostDTO = postService.createPost(postDTO);
        UUID id = createdPostDTO.getId();
        return ResponseEntity.created(URI.create("/post/{id}")).body(createdPostDTO);
    }

    @PutMapping("/post")
    public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO postDTO) {
        PostDTO updatedPost = postService.updatePost(postDTO);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<?> deletePost(@PathVariable UUID id) {
        if (postService.deletePost(id)) return ResponseEntity.noContent().build();
        else return ResponseEntity.badRequest().build();
    }
}
