package ch.m1m.hz.server;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientConnectionStrategyConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HelloWorldCallee {
    public static void main(String[] args) {
        ClientConfig helloWorldConfig = new ClientConfig();
        helloWorldConfig.setClusterName("cubegrid");

        helloWorldConfig.getNetworkConfig().addAddress("172.20.11.104", "172.20.11.104:5702");

        HazelcastInstance hz = HazelcastClient.newHazelcastClient(helloWorldConfig);

        displayDataInLoop(hz);
    }

    public static void displayDataInLoop(HazelcastInstance hz) {

        while (true) {
            Map<String, String> map = hz.getMap("my-distributed-map");
            System.out.println(map.get("1"));
            System.out.println(map.get("2"));
            System.out.println(map.get("3"));

            System.out.println("waiting for 1 second " + LocalDateTime.now());
            System.out.flush();

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
