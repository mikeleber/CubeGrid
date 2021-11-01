package ch.m1m.hz.client;

import com.hazelcast.core.EntryAdapter;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;

public class CubeMapEntryListener extends EntryAdapter {
    @Override
    public void entryAdded(EntryEvent event) {
        System.out.println("add    " + event.getKey() + " oldValue:" + event.getOldValue() + " newValue:" + event.getValue());
        super.entryAdded(event);
    }

    @Override
    public void entryUpdated(EntryEvent event) {
        System.out.println("update " + event.getKey() + " oldValue:" + event.getOldValue() + " newValue:" + event.getValue());
        super.entryUpdated(event);
    }
}
