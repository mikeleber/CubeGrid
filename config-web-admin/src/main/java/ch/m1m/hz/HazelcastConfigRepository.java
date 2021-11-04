package ch.m1m.hz;

import ch.m1m.config.api.Config;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HazelcastConfigRepository {

    private static final Logger LOG = LoggerFactory.getLogger(HazelcastConfigRepository.class);

    private static HazelcastInstance hz;

    private static ObjectMapper objectMapper;

    public HazelcastConfigRepository(@Value("${hazelcast.cluster.name}") final String hzClusterName,
                                     ObjectMapper aObjectMapper) {
        objectMapper = aObjectMapper;

        LOG.info("TRACE: hz server name: {}", hzClusterName);
        LOG.info("TRACE: jackson objectMapper: {}", objectMapper);

        ClientConfig hzClientConfig = new ClientConfig();
        hzClientConfig.setClusterName(hzClusterName);
        //helloWorldConfig.getNetworkConfig().addAddress("172.20.11.104", "172.20.11.104:5702");
        hz = HazelcastClient.newHazelcastClient(hzClientConfig);
    }

    public void add(Config config) throws JsonProcessingException {
        String mapName = getHzMapNameForApplication(config.getApplId());
        String jsonConfigString = objectMapper.writeValueAsString(config);
        Map<String, String> map = hz.getMap(mapName);
        String key = config.getKey();
        map.put(key, jsonConfigString);
        LOG.info("hz ADD map={} key={} value={}", mapName, key, jsonConfigString);
    }

    public String getHzMapNameForApplication(String applId) {
        return "config_app_" + applId;
    }

    public void clearAll(String applId) {
        String mapName = getHzMapNameForApplication(applId);
        Map<String, String> map = hz.getMap(mapName);
        map.clear();
    }
}

/*

String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
Car car = objectMapper.readValue(json, Car.class);

*/
