package ai.FeedService.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/")
    public String demo() {
        return "Demo endpoint for feed is working!";
    }
}
