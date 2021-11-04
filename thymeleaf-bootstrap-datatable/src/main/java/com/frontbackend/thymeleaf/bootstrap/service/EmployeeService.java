package com.frontbackend.thymeleaf.bootstrap.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frontbackend.thymeleaf.bootstrap.model.Employee;
import com.frontbackend.thymeleaf.bootstrap.model.EmployeeComparators;
import com.frontbackend.thymeleaf.bootstrap.model.paging.Column;
import com.frontbackend.thymeleaf.bootstrap.model.paging.Order;
import com.frontbackend.thymeleaf.bootstrap.model.paging.Page;
import com.frontbackend.thymeleaf.bootstrap.model.paging.PageArray;
import com.frontbackend.thymeleaf.bootstrap.model.paging.PagingRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeService {

    private static final Comparator<Employee> EMPTY_COMPARATOR = (e1, e2) -> 0;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");



    private List<String> toStringList(Employee employee) {
        return Arrays.asList(employee.getKey(), employee.getApplId(), employee.getValue(), sdf.format(employee.getChange_Date()),
                 employee.getDescription());
    }

    public Page<Employee> getEmployees(PagingRequest pagingRequest) {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            List<Employee> employees = objectMapper.readValue(getClass().getClassLoader()
                            .getResourceAsStream("employees.json"),
                    new TypeReference<List<Employee>>() {
                    });

            return getPage(employees, pagingRequest);

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return new Page<>();
    }

    private Page<Employee> getPage(List<Employee> employees, PagingRequest pagingRequest) {
        List<Employee> filtered = employees.stream()
                .sorted(sortEmployees(pagingRequest))
                .filter(filterEmployees(pagingRequest))
                .skip(pagingRequest.getStart())
                .limit(pagingRequest.getLength())
                .collect(Collectors.toList());

        long count = employees.stream()
                .filter(filterEmployees(pagingRequest))
                .count();

        Page<Employee> page = new Page<>(filtered);
        page.setRecordsFiltered((int) count);
        page.setRecordsTotal((int) count);
        page.setDraw(pagingRequest.getDraw());

        return page;
    }

    private Predicate<Employee> filterEmployees(PagingRequest pagingRequest) {
        if (pagingRequest.getSearch() == null || StringUtils.isEmpty(pagingRequest.getSearch()
                .getValue())) {
            return employee -> true;
        }

        String value = pagingRequest.getSearch()
                .getValue();

        return employee -> employee.getKey()
                .toLowerCase()
                .contains(value)
                || employee.getApplId()
                .toLowerCase()
                .contains(value)
                || employee.getValue()
                .toLowerCase()
                .contains(value)
                || (employee.getDescription() != null ? employee.getDescription() : "")
                .toLowerCase()
                .contains(value);
    }

    private Comparator<Employee> sortEmployees(PagingRequest pagingRequest) {
        if (pagingRequest.getOrder() == null) {
            return EMPTY_COMPARATOR;
        }

        try {
            Order order = pagingRequest.getOrder()
                    .get(0);

            int columnIndex = order.getColumn();
            Column column = pagingRequest.getColumns()
                    .get(columnIndex);

            Comparator<Employee> comparator = EmployeeComparators.getComparator(column.getData(), order.getDir());
            if (comparator == null) {
                return EMPTY_COMPARATOR;
            }

            return comparator;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return EMPTY_COMPARATOR;
    }
}
