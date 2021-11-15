package ch.m1m.hz.server;

import ch.m1m.config.api.Config;
import com.google.gson.Gson;
import com.hazelcast.config.ClasspathYamlConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HazelcastClusterMain {
    public static boolean istRunning = true;

    public static void main(String[] args) {

        ClasspathYamlConfig helloWorldConfig = new ClasspathYamlConfig("hazelcast.yaml");
        System.out.println(System.getProperty("user.dir"));
        HazelcastInstance hz = Hazelcast.newHazelcastInstance(helloWorldConfig);

        /*
        HazelcastInstance hz2 = Hazelcast.newHazelcastInstance(helloWorldConfig);
        HazelcastInstance hz3 = Hazelcast.newHazelcastInstance(helloWorldConfig);
        */

        Map<String, String> map = generateDataIfNotExists(hz);

        readDataFromCLI(map);
    }

    private static void readDataFromCLI(Map<String, String> map) {
        String content = null;
        while (!"quit".equalsIgnoreCase(content)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                content = reader.readLine();
                System.out.println(content);
                if ("stop".equalsIgnoreCase(content)) {
                    istRunning = false;
                }
                System.out.println(map.put("3", content));
            } catch (Exception e) {
            }
        }
    }

    private static Map<String, String> generateDataIfNotExists(HazelcastInstance hz) {
        Map<String, String> map = hz.getMap("config_app_example");

        createConfigValueOnce(map, "FEATURE_VER_WITH_APPNAME", "false");
        createConfigValueOnce(map, "FEATURE_VER_WITH_BRANCH", "false");
        createConfigValueOnce(map, "FEATURE_VER_WITH_ID", "false");

        createConfigValueOnce(map, "example.version.update.interval", "PT1.000S");

        return map;
    }

    private static void createConfigValueOnce(Map<String, String> map, String key, String value) {
        final String applName = "example";
        final Gson gson = new Gson();
        if (!map.containsKey(key)) {

            // create pojo
            Config configEntry = new Config();
            configEntry.setApplId(applName);
            configEntry.setKey(key);
            configEntry.setValue(value);

            // convert to json
            String configAsJson = gson.toJson(configEntry);

            // add to map
            map.put(key, configAsJson);
        }
    }

    private static Map<String, String> generateData(HazelcastInstance hz) {
        Map<String, String> map = hz.getMap("config-app-toto1");

        while (istRunning) {
            map.put("1", "John");
            map.put("2", "Mary");
            map.put("3", "Jane" + new Date());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
