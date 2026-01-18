package ai.ripple.PostService.Dto.AI;

import java.util.List;

import ai.ripple.PostService.Dto.Post.PostResponse;
import lombok.Data;

@Data
public class GenerateResponse {
    private List<PostResponse> posts;
}
