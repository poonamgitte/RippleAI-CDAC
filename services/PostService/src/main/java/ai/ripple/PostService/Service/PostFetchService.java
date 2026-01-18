package ai.ripple.PostService.Service;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ai.ripple.PostService.Dto.Post.CreatePostRequest;

@Service
public class PostFetchService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PostService postService;

    @Async
    public CompletableFuture<Void> fetchAndSavePostsAsync() {

        String aiServiceUrl = "http://localhost:8080/ai/generate-posts";

        try {
            CreatePostRequest generatedPosts = restTemplate.getForObject(aiServiceUrl, CreatePostRequest.class);

            if (generatedPosts != null && generatedPosts.getPosts() != null && !generatedPosts.getPosts().isEmpty()) {
                postService.savePosts(generatedPosts);
                System.out.println("Posts saved successfully!");
            } else {
                System.out.println("No posts received from AI service");
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch posts: " + e.getMessage());
        }

        return CompletableFuture.completedFuture(null);
    }
}
