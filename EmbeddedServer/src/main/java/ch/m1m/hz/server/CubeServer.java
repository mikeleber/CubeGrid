package ch.m1m.hz.server;

import com.hazelcast.config.ClasspathYamlConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CubeServer {
    public static boolean istRunning = true;

    public static void main(String[] args) {

        // Config helloWorldConfig = new Config();
//        helloWorldConfig.setClusterName("hello-world");

        ClasspathYamlConfig helloWorldConfig = new ClasspathYamlConfig("hazelcast.yaml");
        System.out.println(System.getProperty("user.dir"));
        HazelcastInstance hz = Hazelcast.newHazelcastInstance(helloWorldConfig);
        HazelcastInstance hz2 = Hazelcast.newHazelcastInstance(helloWorldConfig);
        HazelcastInstance hz3 = Hazelcast.newHazelcastInstance(helloWorldConfig);

        Map<String, String> map = generateData(hz);

        System.out.println(map.get("1"));
        System.out.println(map.get("2"));
        System.out.println(map.get("3"));
        // Enter data using BufferReader
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

    private static Map<String, String> generateData(HazelcastInstance hz) {
        Map<String, String> map = hz.getMap("my-distributed-map");

        while (istRunning) {
            map.put("1", "John");
            map.put("2", "Mary");
            map.put("3", "Jane"+new Date());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
