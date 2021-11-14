package com.frontbackend.thymeleaf.bootstrap.service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.frontbackend.thymeleaf.bootstrap.Application;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frontbackend.thymeleaf.bootstrap.model.ApplicationProperty;
import com.frontbackend.thymeleaf.bootstrap.model.ApplicationPropertyComparators;
import com.frontbackend.thymeleaf.bootstrap.model.paging.Column;
import com.frontbackend.thymeleaf.bootstrap.model.paging.Order;
import com.frontbackend.thymeleaf.bootstrap.model.paging.Page;
import com.frontbackend.thymeleaf.bootstrap.model.paging.PagingRequest;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class ApplicationPropertyService {
    private HazelcastInstance hz;
    private IMap<String, String> appPropertyMap;
    private static final Comparator<ApplicationProperty> EMPTY_COMPARATOR = (e1, e2) -> 0;
    private static ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");


    private List<String> toStringList(ApplicationProperty appProperty) {
        return Arrays.asList(appProperty.getKey(), appProperty.getApplId(), appProperty.getValue(), sdf.format(appProperty.getChange_Date()),
                appProperty.getDescription());
    }

    public Page<ApplicationProperty> getAppProperties(PagingRequest pagingRequest) {
        return getPage(toPropertyList(appPropertyMap), pagingRequest);
    }
    public void updateAppProperties(ApplicationProperty property) throws JsonProcessingException {
        appPropertyMap.put(property.getKey(),objectMapper.writeValueAsString(property));

    }
    @PostConstruct
    public void initialize() {
        ClientConfig helloWorldConfig = new ClientConfig();
        helloWorldConfig.setClusterName(Application.HZ_CLUSTER_NAME);
        helloWorldConfig.getNetworkConfig().addAddress("127.0.0.1");
        CompletableFuture.runAsync(() -> {
            hz = HazelcastClient.newHazelcastClient(helloWorldConfig);
            appPropertyMap = hz.getMap("config_app_example");
        });


    }

    public static List<ApplicationProperty> toPropertyList(IMap<String, ?> map) {
        if (map == null) {
            return new ArrayList<>();
        }
        AtomicInteger  rowID = new AtomicInteger();
        List<ApplicationProperty> mapAsString = map.keySet().stream()
                .map(key -> propertyFromEntry(rowID.incrementAndGet(), key, map.get(key)))
                .collect(Collectors.toList());
        return mapAsString;
    }

    private static ApplicationProperty propertyFromEntry(int rowID, String key, Object o) {
        ApplicationProperty aProp = null;
        try {
            aProp = objectMapper.readValue(o.toString(), ApplicationProperty.class);
            aProp.setId(rowID);
            aProp.setChange_Date(new Date());
            return aProp;
        } catch (JsonProcessingException jpe) {
            jpe.printStackTrace();
        }


        return aProp;
    }

    public static String convertWithStream(Map<String, ?> map) {
        String mapAsString = map.keySet().stream()
                .map(key -> key + "=" + map.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
        return mapAsString;
    }

    private Page<ApplicationProperty> getPage(List<ApplicationProperty> applicationProperties, PagingRequest pagingRequest) {
        List<ApplicationProperty> filtered = applicationProperties.stream()
                .sorted(sortAppProperties(pagingRequest))
                .filter(filterAppProperties(pagingRequest))
                .skip(pagingRequest.getStart())
                .limit(pagingRequest.getLength())
                .collect(Collectors.toList());

        long count = applicationProperties.stream()
                .filter(filterAppProperties(pagingRequest))
                .count();

        Page<ApplicationProperty> page = new Page<>(filtered);
        page.setRecordsFiltered((int) count);
        page.setRecordsTotal((int) count);
        page.setDraw(pagingRequest.getDraw());

        return page;
    }

    private Predicate<ApplicationProperty> filterAppProperties(PagingRequest pagingRequest) {
        if (pagingRequest.getSearch() == null || StringUtils.isEmpty(pagingRequest.getSearch()
                .getValue())) {
            return employee -> true;
        }

        String value = pagingRequest.getSearch()
                .getValue().toLowerCase();

        return aProperty -> aProperty.getKey()
                .toLowerCase()
                .contains(value)
                ||  aProperty.getValue()
                .toLowerCase()
                .contains(value)
                || (aProperty.getDescription() != null ? aProperty.getDescription() : "")
                .toLowerCase()
                .contains(value);
    }

    private Comparator<ApplicationProperty> sortAppProperties(PagingRequest pagingRequest) {
        if (pagingRequest.getOrder() == null) {
            return EMPTY_COMPARATOR;
        }

        try {
            Order order = pagingRequest.getOrder()
                    .get(0);

            int columnIndex = order.getColumn();
            Column column = pagingRequest.getColumns()
                    .get(columnIndex);

            Comparator<ApplicationProperty> comparator = ApplicationPropertyComparators.getComparator(column.getData(), order.getDir());
            if (comparator == null) {
                return EMPTY_COMPARATOR;
            }

            return comparator;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return EMPTY_COMPARATOR;
    }

    public void deleteProperty(ApplicationProperty property) {
        appPropertyMap.remove(property.getKey());
    }
}
