package com.frontbackend.thymeleaf.bootstrap.model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.frontbackend.thymeleaf.bootstrap.model.paging.Direction;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

public final class EmployeeComparators {

    @EqualsAndHashCode
    @AllArgsConstructor
    @Getter
    static class Key {
        String name;
        Direction dir;
    }

    static Map<Key, Comparator<Employee>> map = new HashMap<>();

    static {
        map.put(new Key("key", Direction.asc), Comparator.comparing(Employee::getKey));
        map.put(new Key("key", Direction.desc), Comparator.comparing(Employee::getKey)
                                                           .reversed());

        map.put(new Key("start_date", Direction.asc), Comparator.comparing(Employee::getChange_Date));
        map.put(new Key("start_date", Direction.desc), Comparator.comparing(Employee::getChange_Date)
                                                                 .reversed());

        map.put(new Key("applId", Direction.asc), Comparator.comparing(Employee::getApplId));
        map.put(new Key("applId", Direction.desc), Comparator.comparing(Employee::getApplId)
                                                               .reversed());
        map.put(new Key("description", Direction.asc), Comparator.comparing(Employee::getDescription));
        map.put(new Key("description", Direction.desc), Comparator.comparing(Employee::getDescription)
                                                               .reversed());

        map.put(new Key("value", Direction.asc), Comparator.comparing(Employee::getValue));
        map.put(new Key("value", Direction.desc), Comparator.comparing(Employee::getValue)
                                                             .reversed());

        map.put(new Key("extn", Direction.asc), Comparator.comparing(Employee::getExtn));
        map.put(new Key("extn", Direction.desc), Comparator.comparing(Employee::getExtn)
                                                           .reversed());
    }

    public static Comparator<Employee> getComparator(String name, Direction dir) {
        return map.get(new Key(name, dir));
    }

    private EmployeeComparators() {
    }
}
