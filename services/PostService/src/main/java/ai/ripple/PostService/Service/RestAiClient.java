package ai.ripple.PostService.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ai.ripple.PostService.Dto.AI.GenerateRequest;
import ai.ripple.PostService.Dto.AI.GenerateResponse;
import ai.ripple.PostService.Dto.Post.PostResponse;
import ai.ripple.PostService.Interfaces.AIClient;

@Service
public class RestAiClient implements AIClient {

    private final RestTemplate restTemplate;

    private final String aiBaseUrl;

    public RestAiClient(RestTemplate restTemplate,
                        @Value("${ai.service.url}") String aiBaseUrl) {
        this.restTemplate = restTemplate;
        this.aiBaseUrl = aiBaseUrl;
    }

    @Override
    public List<PostResponse> generatePosts(String prompt) {

        GenerateRequest request = new GenerateRequest();
        request.setPrompt(prompt);

        GenerateResponse response = restTemplate.postForObject(
                aiBaseUrl + "/ai/generate-posts",
                request,
                GenerateResponse.class
        );

        return response != null ? response.getPosts() : List.of();
    }

    @Override
    public double rankPost(String content) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rankPost'");
    }

    @Override
    public String chatWithData(String question) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'chatWithData'");
    }

   
}