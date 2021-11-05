package ch.m1m.hz;

import ch.m1m.config.api.Config;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ApiV1Controller {

    private static final Logger LOG = LoggerFactory.getLogger(ApiV1Controller.class);

    private HazelcastConfigRepository hzConfigRepository;

    @Autowired
    public ApiV1Controller(HazelcastConfigRepository hzConfigRepository) {
        this.hzConfigRepository = hzConfigRepository;
    }

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
    public List<Config> getConfigSingle(@RequestParam(required = false) Optional<Boolean> create,
                                        @RequestParam(required = false) Optional<Boolean> clear
    ) throws JsonProcessingException {

        LOG.info("GET called /config create={} clear={}", create, clear);
        String applId = "example";

        if (clear.isPresent()) hzConfigRepository.clearAll(applId);

        if (create.isPresent()) {
            createConfigForApplId(applId);
        }

        return hzConfigRepository.getAll(applId);
    }

    private void createConfigForApplId(String applId) {

        try {
            hzConfigRepository.add(new Config(applId, "AllowAdminForUsers", "b00123, b12345", null));
            hzConfigRepository.add(new Config(applId, "FormTemplateNewCustomers", "Neukunden_v12", null));
            hzConfigRepository.add(new Config(applId, "feature.UI_DARKMODE", "true", null));
            hzConfigRepository.add(new Config(applId, "feature.LOG_FORMAT_JSON", "false", null));
            hzConfigRepository.add(new Config(applId, "feature.TIME_FORMAT_24H", "true", null));

        } catch (JsonProcessingException e) {
            LOG.error("generate test data failed", e);
        }
    }

    @PostMapping(path = "/api/v1/config",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response createOrUpdateConfig(@RequestBody Config config) {

        LOG.info("POST called /config");


        return Response.ok().build();
    }
}
