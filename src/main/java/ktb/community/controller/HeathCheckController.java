package ktb.community.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeathCheckController {
    @GetMapping("/health")
    public ResponseEntity.BodyBuilder getHealthcheck() {
        return ResponseEntity.ok();
    }

    @GetMapping("/check/rolling")
    public String checkRolling() { return "hello v1 World!"; }
}

