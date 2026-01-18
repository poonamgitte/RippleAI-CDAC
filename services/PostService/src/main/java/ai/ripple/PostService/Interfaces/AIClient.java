package ai.ripple.PostService.Interfaces;

import java.util.List;

import ai.ripple.PostService.Dto.Post.PostResponse;

public interface AIClient {
    
    List<PostResponse> generatePosts(String prompt);
    
    double rankPost(String content);

    String chatWithData(String question);
}
