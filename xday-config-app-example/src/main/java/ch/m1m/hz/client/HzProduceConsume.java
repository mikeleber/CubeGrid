package ch.m1m.hz.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

import java.util.Map;

public class HzProduceConsume {

    public static void main(String ...args) {

        // client setup
        //
        ClientConfig hzConfig = new ClientConfig();
        hzConfig.setClusterName("xdayconfig");

        // cluster join
        //
        HazelcastInstance hz = HazelcastClient.newHazelcastClient(hzConfig);

        // get reference to cluster shared component
        //
        Map<String, String> map = hz.getMap("config_app_example");

        // GET existing key
        //
        String key = "example.version.update.interval";
        String intervalMapValue = map.get(key);
        System.out.println(key + ": " + intervalMapValue);

        // PUT / GET a new key/value
        //
        key = "myKey";
        map.put(key, "myValue");
        String value = map.get(key);
        System.out.println(key + ": " + value);
    }
}
