package ai.ripple.PostService.util;

import java.util.List;
import ai.ripple.PostService.Dto.Post.PostResponse;

public class FallbackPosts {

    public static final List<PostResponse> DEFAULT_POSTS = List.of(
        new PostResponse(
            "Another 'art installation' on Shivaji Nagar road – a pile of trash that says 'modern sculpture'. 🎨 Let's swap the avant‑garde for a clean canvas. Grab a broom, bring your sarcasm, and maybe a smile. #PuneClean #ShivajiNagar #Sarcasm #Humor #Volunteer",
            "http://localhost:8000/postImages/eeedbb32-5ef8-42fe-bbdc-890f278a81c6.png",
            "http://localhost:8000/songs/preetylittlebaby.mp3",
            "2025-12-20T18:30:00+05:30",
            "IMAGE",
            List.of("PuneClean", "ShivajiNagar", "Sarcasm", "Humor", "Volunteer")
        ),
        new PostResponse(
            "We laughed, we sweated, we turned a mess into a memory. 🌅 Seeing Shivaji Nagar sparkle again feels like finding a lost sock in the laundry – pure joy! Join the feel‑good cleanup crew. #PuneClean #ShivajiNagar #Emotional #CommunityLove #Volunteer",
            "http://localhost:8000/postImages/121e3e57-7ff1-4693-a461-31a3815364eb.png",
            "http://localhost:8000/songs/chandkaSona.mp3",
            "2025-12-21T07:00:00+05:30",
            "IMAGE",
            List.of("PuneClean", "ShivajiNagar", "Emotional", "CommunityLove", "Volunteer")
        )
    );
}
