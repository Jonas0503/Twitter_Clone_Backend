package clone.twitter.service;

import clone.twitter.dto.PostDTO;
import clone.twitter.models.Post;
import clone.twitter.repo.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {

    private AppUserService appUserService;
    private PostRepository postRepository;

    @Autowired
    public PostService(AppUserService appUserService, PostRepository postRepository) {
        this.appUserService = appUserService;
        this.postRepository = postRepository;
    }

    public PostDTO mapToPostDTO(Post post) {
        PostDTO postDTO = new PostDTO();

        postDTO.setId(post.getId());
        postDTO.setText(post.getText());
        postDTO.setAppUser(appUserService.mapToAppUserDTO(post.getAppUser()));
        postDTO.setLiked(appUserService.createAppUserDTOList(post.getLiked()));
        postDTO.setDisliked(appUserService.createAppUserDTOList(post.getDisliked()));
        postDTO.setParent(mapToPostDTO(post));

        List<PostDTO> postDTOList = new ArrayList<>();
        for (Post comment : post.getComments()) {
            postDTOList.add(mapToPostDTO(comment));
        }
        postDTO.setComments(postDTOList);

        return postDTO;
    }

    public Post mapToPostEntity(PostDTO postDTO) {
        Post post = new Post();

        post.setId(postDTO.getId());
        post.setText(postDTO.getText());
        post.setAppUser(appUserService.mapToAppUserEntity(postDTO.getAppUser()));
        post.setLiked(appUserService.createAppUserEntityList(postDTO.getLiked()));
        post.setDisliked(appUserService.createAppUserEntityList(postDTO.getDisliked()));
        post.setParent(mapToPostEntity(postDTO));

        List<Post> postEntityList = new ArrayList<>();
        for (PostDTO comment : postDTO.getComments()) {
            postEntityList.add(mapToPostEntity(comment));
        }
        post.setComments(postEntityList);

        return post;
    }

    public List<PostDTO> createPostDTOList(List<Post> postList) {
        List<PostDTO> postDTOList = new ArrayList<>();

        for (Post post : postList) {
            postDTOList.add(mapToPostDTO(post));
        }

        return postDTOList;
    }

    public List<Post> createPostEntityList(List<PostDTO> postDTOList) {
        List<Post> postList = new ArrayList<>();

        for (PostDTO postDTO : postDTOList) {
            postList.add(mapToPostEntity(postDTO));
        }

        return postList;
    }

    // TODO: Exception Handling
    @Transactional
    public PostDTO createPost(PostDTO postDTO) {
        if (postRepository.existsById(postDTO.getId())) {
            throw new RuntimeException("Post already exists!");
        }
        else {
            Post post = mapToPostEntity(postDTO);
            Post createdPost = postRepository.save(post);

            return mapToPostDTO(createdPost);
        }
    }

    @Transactional
    public PostDTO readPost(UUID uuid) {
        Optional<Post> postOptional = postRepository.findById(uuid);

        if (postOptional.isEmpty()) {
            throw new RuntimeException("Exception Handling not yet implemented! AppUser does not exists!");
        }
        else {
            Post post = postOptional.get();
            return mapToPostDTO(post);
        }
    }

    @Transactional
    public PostDTO updatePost(PostDTO postDTO) {
        if (postRepository.existsById(postDTO.getId())) {
            Post post = mapToPostEntity(postDTO);
            Post updatedPost = postRepository.save(post);

            return mapToPostDTO(updatedPost);
        }
        else {
            throw new RuntimeException("Post does not exist!");
        }
    }

    @Transactional
    public boolean deletePost(UUID uuid) {
        if (postRepository.existsById(uuid)) {
            postRepository.deleteById(uuid);
            return true;
        }
        else {
            return false;
        }
    }
}
