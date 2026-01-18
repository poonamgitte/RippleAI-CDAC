package ai.ripple.PostService.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import ai.ripple.PostService.Dto.Post.PostResponse;
import ai.ripple.PostService.Interfaces.AIClient;
import ai.ripple.PostService.util.FallbackPosts;

@Service
public class PostGenerationService {
    
    private final AIClient aiClient;

    public PostGenerationService(AIClient aiClient) {
        this.aiClient = aiClient;
    }

    public List<PostResponse> generate(String prompt) {
        List<PostResponse> posts = aiClient.generatePosts(prompt);
         
        if (posts == null || posts.isEmpty()) {
            posts = FallbackPosts.DEFAULT_POSTS;
        }
          
        return posts;
    }
}
