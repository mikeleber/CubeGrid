package ch.m1m.hz.client;

import com.hazelcast.core.EntryAdapter;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;

import java.util.Map;
import java.util.stream.Collectors;

public class CubeMapEntryListener extends EntryAdapter {
    @Override
    public void entryAdded(EntryEvent event) {
        System.out.println("eventObserver: add    " + event.getKey() + " oldValue:" + event.getOldValue() + " newValue:" + event.getValue());
        super.entryAdded(event);
    }

    @Override
    public void entryUpdated(EntryEvent event) {
        System.out.println("eventObserver: update " + event.getKey() + " oldValue:" + event.getOldValue() + " newValue:" + event.getValue());
        super.entryUpdated(event);
    }

    @Override
    public void entryRemoved(EntryEvent event) {
        System.out.println("eventObserver: remove " + event.getKey());
        super.entryRemoved(event);
    }
}
