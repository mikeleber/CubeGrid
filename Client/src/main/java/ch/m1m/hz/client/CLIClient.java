package ch.m1m.hz.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastException;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.map.listener.MapListener;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CLIClient {
   public static  HazelcastInstance hz ;
  public static void main(String[] args) {
      ClientConfig helloWorldConfig = new ClientConfig();
      helloWorldConfig.setClusterName("cubegrid");

      //helloWorldConfig.getNetworkConfig().addAddress("172.20.11.104", "172.20.11.104:5702");

       hz = HazelcastClient.newHazelcastClient(helloWorldConfig);
      listenData(hz);
      IMap<String, String> map = hz.getMap("my-distributed-map");

    while (true){
        convertWithStream(map);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

  }
    public static String convertWithStream(Map<String, ?> map) {
        String mapAsString = map.keySet().stream()
                .map(key -> key + "=" + map.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
        return mapAsString;
    }
  private static void listenData(HazelcastInstance hz) {
   IMap<String, String> map = hz.getMap("my-distributed-map");

      map.addEntryListener(new CubeMapEntryListener() {
      }, true);
  }
}
