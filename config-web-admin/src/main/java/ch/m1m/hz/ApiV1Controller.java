package ch.m1m.hz;

import ch.m1m.config.api.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ApiV1Controller {

    private static final Logger LOG = LoggerFactory.getLogger(ApiV1Controller.class);

    @GetMapping("/api/v1/ok")
    String retStaticOK() {
        return "ok";
    }

    @GetMapping(path = "/api/v1/config/{key}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Config getConfigSingle(@PathVariable String key) {

        LOG.info("GET called /config/{}", key);

        Config config = new Config();
        config.setApplId("example");
        config.setKey("worflowV2persist");
        config.setValue("true");

        // Boolean b = Boolean.valueOf("true");
        return config;
    }

    @GetMapping(path = "/api/v1/config",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Config> getConfigSingle() {

        LOG.info("GET called /config");

        List<Config> confList = new ArrayList<>();

        confList.add(new Config("example", "key1", "false", null));
        confList.add(new Config("example", "key2", "true", null));

        return confList;
    }

    @PostMapping(path = "/api/v1/config",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response createOrUpdateConfig(@RequestBody Config config) {

        LOG.info("POST called /config");


        return Response.ok().build();
    }
}
