package ai.ripple.PostService.Dto.Post;

import java.util.List;

import ai.ripple.PostService.Entity.OwnerType;
import ai.ripple.PostService.Entity.PostType;
import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Data
public class CreatePostRequest {

    @NotNull(message = "ownerType is required")
    private OwnerType ownerType; // USER / NGO

    @NotBlank(message = "ownerId is required")
    private String ownerId; // userId or ngoId

    @NotNull(message = "postType is required")
    private PostType postType; // NORMAL / CAMPAIGN

    // Required only when postType == CAMPAIGN
    private String campaignId;

    @NotEmpty(message = "posts list cannot be empty")
    @Valid
    private List<CreatePostItem> posts;
}
