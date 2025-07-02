package clone.twitter.controller;

import clone.twitter.dto.TweetDTO;
import clone.twitter.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class TweetController {

    private TweetService tweetService;

    @Autowired
    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @GetMapping("/tweet/{id}")
    public ResponseEntity<TweetDTO> getTweet(@PathVariable UUID id) {
        TweetDTO tweetDTO = tweetService.readTweet(id);
        return ResponseEntity.ok(tweetDTO);
    }

    @PostMapping("/tweet")
    public ResponseEntity<TweetDTO> createNewTweet(@RequestBody TweetDTO tweetDTO) {
        TweetDTO createdTweetDTO = tweetService.createTweet(tweetDTO);
        UUID id = createdTweetDTO.getId();
        return ResponseEntity.created(URI.create("/post/" + id)).body(createdTweetDTO);
    }

    @PutMapping("/tweet")
    public ResponseEntity<TweetDTO> updateTweet(@RequestBody TweetDTO tweetDTO) {
        TweetDTO updatedPost = tweetService.updateTweet(tweetDTO);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/tweet/{id}")
    public ResponseEntity<?> deleteTweet(@PathVariable UUID id) {
        tweetService.deleteTweet(id);
        return ResponseEntity.noContent().build();
    }
}
