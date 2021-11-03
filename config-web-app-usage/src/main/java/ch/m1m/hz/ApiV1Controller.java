package ch.m1m.hz;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiV1Controller {

    @GetMapping("/api/v1/ok")
    String retStaticOK() {
        return "ok";
    }
}
