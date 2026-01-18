package ai.ripple.PostService.Dto.Post;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class CreatePostItem {

    @NotBlank(message = "caption is required")
    @Size(max = 1000)
    private String caption;

    @NotBlank(message = "postLink is required")
    private String postLink;

    private String musicLink;

    private LocalDateTime scheduledAt;

    @Size(max = 20)
    private List<@NotBlank String> tags;
}
