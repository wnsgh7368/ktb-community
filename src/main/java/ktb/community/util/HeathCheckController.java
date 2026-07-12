package ktb.community.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeathCheckController {
    @GetMapping("/aculator/health")
    public ResponseEntity.BodyBuilder getHealthcheck() {
        return ResponseEntity.ok();
    }
}
