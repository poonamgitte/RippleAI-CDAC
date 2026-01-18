package ai.ripple.PostService.Dto.Post;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor          
@NoArgsConstructor 
public class PostResponse {

    private String caption;

    @JsonProperty("post_link")
    private String postLink;

    @JsonProperty("music_link")
    private String musicLink;

    @JsonProperty("schedule_time")
    private String scheduleTime;

    private String type;

    private List<String> tags;
}
