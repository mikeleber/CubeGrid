package ch.m1m.hz.server;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.Map;

public class HelloWorldCallee {
  public static void main(String[] args) {
    ClientConfig helloWorldConfig = new ClientConfig();
    helloWorldConfig.setClusterName("hello-world");
    helloWorldConfig.getNetworkConfig().addAddress("172.20.0.121", "10.80.1.7:5702");

    HazelcastInstance hz = HazelcastClient.newHazelcastClient(helloWorldConfig);


    Map<String, String> map = hz.getMap("my-distributed-map");
    System.out.println(map.get("1"));
    System.out.println(map.get("2"));
    System.out.println(map.get("3"));
  }
}
