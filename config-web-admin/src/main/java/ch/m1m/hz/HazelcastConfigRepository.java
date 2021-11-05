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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public List<Config> getAll(String applId) {
        String mapName = getHzMapNameForApplication(applId);
        List<Config> confList = new ArrayList<>();
        Map<String, String> map = hz.getMap(mapName);
        Set<String> keys = map.keySet();
        keys.stream().forEach(key -> {
            String configJsonValue = map.get(key);
            try {
                Config config = objectMapper.readValue(configJsonValue, Config.class);
                confList.add(config);
            } catch (JsonProcessingException e) {
                LOG.error("failed to parse value from map for key={} value={}", key, configJsonValue);
            }
        });

        return confList;
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
