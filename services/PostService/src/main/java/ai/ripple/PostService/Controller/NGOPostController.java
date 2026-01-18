package ai.ripple.PostService.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ai.ripple.PostService.Entity.OwnerType;
import ai.ripple.PostService.Entity.Post;
import ai.ripple.PostService.Entity.PostStatus;
import ai.ripple.PostService.Kafka.PostProducer;
import ai.ripple.PostService.Service.PostService;

import java.util.List;

@RestController
@RequestMapping("/ngo/posts")
public class NGOPostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostProducer postEventProducer;


    // Get posts by owner
    @GetMapping
    public ResponseEntity<List<Post>> getPostsByOwner(
            @RequestParam String ownerId,
            @RequestParam OwnerType ownerType
    ) {
        return ResponseEntity.ok(
                postService.getPostsByOwner(ownerId, ownerType)
        );
    }

    // Get campaign posts
    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<Post>> getCampaignPosts(
            @PathVariable String campaignId
    ) {
        return ResponseEntity.ok(
                postService.getCampaignPosts(campaignId)
        );
    }

    // ---------------- STATE TRANSITIONS ----------------

    // Approve post (ADMIN / MODERATOR action)
    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approvePost(@PathVariable String id) {

        Post post = postService.getPostById(id);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        if (post.getStatus() != PostStatus.DRAFT &&
            post.getStatus() != PostStatus.SCHEDULED) {
            return ResponseEntity.badRequest()
                    .body("Post cannot be approved in current state");
        }

        post.setStatus(PostStatus.PUBLISHED);
        postService.savePost(post);

        postEventProducer.publish("post-event", post);

        return ResponseEntity.ok("Post approved and published");
    }

    // Reject post
    @DeleteMapping("/{id}/reject")
    public ResponseEntity<?> rejectPost(@PathVariable String id) {

        boolean deleted = postService.deletePost(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("Post rejected and deleted");
    }

    // ---------------- BULK SUBMISSION ----------------

    // Submit posts for publishing (bulk)
    @PostMapping("/submit")
    public ResponseEntity<?> submitPosts(
            @RequestBody List<String> postIds
    ) {
        List<Post> posts = postService.getPostsByIds(postIds);

        if (posts.isEmpty()) {
            return ResponseEntity.badRequest().body("No valid posts found");
        }

        for (Post post : posts) {

            if (post.getStatus() != PostStatus.SCHEDULED) {
                continue; // skip invalid state
            }

            post.setStatus(PostStatus.PUBLISHED);
            postService.savePost(post);
            postEventProducer.publish("post-event", post);
        }

        return ResponseEntity.ok("Submitted " + posts.size() + " posts");
    }
}
