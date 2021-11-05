package com.frontbackend.thymeleaf.bootstrap.model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.frontbackend.thymeleaf.bootstrap.model.paging.Direction;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

public final class ApplicationPropertyComparators {

    @EqualsAndHashCode
    @AllArgsConstructor
    @Getter
    static class Key {
        String name;
        Direction dir;
    }

    static Map<Key, Comparator<ApplicationProperty>> map = new HashMap<>();

    static {
        map.put(new Key("key", Direction.asc), Comparator.comparing(ApplicationProperty::getKey));
        map.put(new Key("key", Direction.desc), Comparator.comparing(ApplicationProperty::getKey)
                                                           .reversed());

        map.put(new Key("start_date", Direction.asc), Comparator.comparing(ApplicationProperty::getChange_Date));
        map.put(new Key("start_date", Direction.desc), Comparator.comparing(ApplicationProperty::getChange_Date)
                                                                 .reversed());

        map.put(new Key("applId", Direction.asc), Comparator.comparing(ApplicationProperty::getApplId));
        map.put(new Key("applId", Direction.desc), Comparator.comparing(ApplicationProperty::getApplId)
                                                               .reversed());
        map.put(new Key("description", Direction.asc), Comparator.comparing(ApplicationProperty::getDescription));
        map.put(new Key("description", Direction.desc), Comparator.comparing(ApplicationProperty::getDescription)
                                                               .reversed());

        map.put(new Key("value", Direction.asc), Comparator.comparing(ApplicationProperty::getValue));
        map.put(new Key("value", Direction.desc), Comparator.comparing(ApplicationProperty::getValue)
                                                             .reversed());

        map.put(new Key("extn", Direction.asc), Comparator.comparing(ApplicationProperty::getExtn));
        map.put(new Key("extn", Direction.desc), Comparator.comparing(ApplicationProperty::getExtn)
                                                           .reversed());
    }

    public static Comparator<ApplicationProperty> getComparator(String name, Direction dir) {
        return map.get(new Key(name, dir));
    }

    private ApplicationPropertyComparators() {
    }
}
