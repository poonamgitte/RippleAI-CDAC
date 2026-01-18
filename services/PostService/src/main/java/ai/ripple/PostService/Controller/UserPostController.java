package ai.ripple.PostService.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ai.ripple.PostService.Dto.Post.CreatePostItem;
import ai.ripple.PostService.Dto.Post.CreatePostRequest;
import ai.ripple.PostService.Entity.OwnerType;
import ai.ripple.PostService.Entity.Post;
import ai.ripple.PostService.Entity.PostType;
import ai.ripple.PostService.Service.PostService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user/posts")
public class UserPostController {

    @Autowired
    private PostService postService;

    // ----------------- CREATE POST -----------------
    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody CreatePostRequest request) {

        // Normal users can only create NORMAL posts
        if (request.getOwnerType() == OwnerType.USER) {
            request.setPostType(PostType.NORMAL);
            request.setCampaignId(null); // USERS cannot attach campaigns
        }

        postService.savePosts(request);
        return ResponseEntity.ok("Post(s) created successfully");
    }

    // ----------------- READ POSTS -----------------
    @GetMapping
    public ResponseEntity<List<Post>> getMyPosts(
            @RequestParam String ownerId,
            @RequestParam OwnerType ownerType
    ) {
        List<Post> posts = postService.getPostsByOwner(ownerId, ownerType);
        if (posts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(posts);
    }

    // ----------------- UPDATE POST -----------------
    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(
            @PathVariable String postId,
            @Valid @RequestBody CreatePostItem updateRequest,
            @RequestParam String ownerId,
            @RequestParam OwnerType ownerType
    ) {
        Post post = postService.getPostById(postId);

        if (post == null) return ResponseEntity.notFound().build();

        // Only owner can update their post
        if (!post.getOwnerId().equals(ownerId) || post.getOwnerType() != ownerType) {
            return ResponseEntity.status(403).body("Not allowed to update this post");
        }

        // Normal users can only update NORMAL posts
        if (ownerType == OwnerType.USER && post.getPostType() != PostType.NORMAL) {
            return ResponseEntity.status(403).body("Users cannot update campaign posts");
        }

        // Update allowed fields
        post.setCaption(updateRequest.getCaption());
        post.setPostLink(updateRequest.getPostLink());
        post.setMusicLink(updateRequest.getMusicLink());
        post.setTags(updateRequest.getTags());
        post.setScheduledAt(updateRequest.getScheduledAt());

        postService.savePost(post);
        return ResponseEntity.ok("Post updated successfully");
    }

    // ----------------- DELETE POST -----------------
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(
            @PathVariable String postId,
            @RequestParam String ownerId,
            @RequestParam OwnerType ownerType
    ) {
        Post post = postService.getPostById(postId);

        if (post == null) return ResponseEntity.notFound().build();

        // Only owner can delete their post
        if (!post.getOwnerId().equals(ownerId) || post.getOwnerType() != ownerType) {
            return ResponseEntity.status(403).body("Not allowed to delete this post");
        }

        // Normal users can only delete NORMAL posts
        if (ownerType == OwnerType.USER && post.getPostType() != PostType.NORMAL) {
            return ResponseEntity.status(403).body("Users cannot delete campaign posts");
        }

        postService.deletePost(postId);
        return ResponseEntity.ok("Post deleted successfully");
    }
}
