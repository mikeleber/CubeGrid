package ch.m1m.hz.client;

import com.google.gson.Gson;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import javax.json.Json;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class CLIClient {
    public static HazelcastInstance hz;

    private static String HZ_CONF_APP_MAP = "config_app_example";

    public static void main(String[] args) {
        ClientConfig hzConfig = new ClientConfig();
        hzConfig.setClusterName("xdayconfig");

        //helloWorldConfig.getNetworkConfig().addAddress("172.20.11.104", "172.20.11.104:5702");

        hz = HazelcastClient.newHazelcastClient(hzConfig);

        listenData(hz);

        IMap<String, String> map = hz.getMap(HZ_CONF_APP_MAP);

        while (true) {

            Duration sleepDuration = evalDurationFromMap(map, "example.version.update.interval");
            printVersion(map);

            try {
                TimeUnit.MILLISECONDS.sleep(sleepDuration.toMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void listenData(HazelcastInstance hz) {
        IMap<String, String> map = hz.getMap(HZ_CONF_APP_MAP);
        map.addEntryListener(new CubeMapEntryListener() {
        }, true);
    }

    private static void printVersion(IMap<String, String> map) {

        String versionValue = "1.1.9";
        String branchValue = "master";
        String commitIdValue = "f09f17483fefb988a2dc803fa1ea6ff5f9a7c239";
        String applNameValue = "example";

        var jsonObjBuilder = Json.createObjectBuilder();

        jsonObjBuilder.add("version", versionValue);

        if (evalBooleanFromMap(map, "FEATURE_VER_WITH_APPNAME")) {
            jsonObjBuilder.add("applicaton", applNameValue);
        }

        if (evalBooleanFromMap(map, "FEATURE_VER_WITH_BRANCH")) {
            jsonObjBuilder.add("branch", branchValue);
        }

        if (evalBooleanFromMap(map, "FEATURE_VER_WITH_ID")) {
            jsonObjBuilder.add("commitId", commitIdValue);
        }

        var jsonObjVersion = jsonObjBuilder.build();
        var versionAsJsonString = jsonObjVersion.toString();

        LocalDateTime ldt = LocalDateTime.now();
        System.out.println(ldt.toString() + " print version strucure: " + versionAsJsonString);
    }

    /* parse the 'value' key from the returned JSON object
     *
     */
    private static String evalStringFromMap(IMap<String, String> map, String feature_ver_with_appname) {
        boolean rc = false;
        String mapEntryValue = map.get(feature_ver_with_appname);
        Gson gson = new Gson();
        ch.m1m.config.api.Config config = gson.fromJson(mapEntryValue, ch.m1m.config.api.Config.class);
        return config.getValue();
    }

    private static boolean evalBooleanFromMap(IMap<String, String> map, String key) {
        String boolValue = evalStringFromMap(map, key);
        return Boolean.parseBoolean(boolValue);
    }

    private static Duration evalDurationFromMap(IMap<String, String> map, String key) {
        String value = evalStringFromMap(map, key);
        return Duration.parse(value);
    }
}
