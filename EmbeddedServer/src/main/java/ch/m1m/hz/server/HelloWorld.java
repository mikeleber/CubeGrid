package ch.m1m.hz.server;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.Map;

public class HelloWorld {
  public static void main(String[] args) {
    Config helloWorldConfig = new Config();
    helloWorldConfig.setClusterName("hello-world");


    HazelcastInstance hz = Hazelcast.newHazelcastInstance(helloWorldConfig);
    HazelcastInstance hz2 = Hazelcast.newHazelcastInstance(helloWorldConfig);
    HazelcastInstance hz3 = Hazelcast.newHazelcastInstance(helloWorldConfig);

    Map<String, String> map = hz.getMap("my-distributed-map");
    map.put("1", "John");
    map.put("2", "Mary");
    map.put("3", "Jane");

    System.out.println(map.get("1"));
    System.out.println(map.get("2"));
    System.out.println(map.get("3"));
  }
}
