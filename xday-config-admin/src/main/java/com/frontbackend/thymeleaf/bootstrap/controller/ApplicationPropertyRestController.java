package com.frontbackend.thymeleaf.bootstrap.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.frontbackend.thymeleaf.bootstrap.model.ApplicationProperty;
import com.frontbackend.thymeleaf.bootstrap.model.paging.Page;
import com.frontbackend.thymeleaf.bootstrap.model.paging.PagingRequest;
import com.frontbackend.thymeleaf.bootstrap.service.ApplicationPropertyService;

@RestController
@RequestMapping("applicationProperties")
public class ApplicationPropertyRestController {

    private final ApplicationPropertyService applicationPropertyService;

    @Autowired
    public ApplicationPropertyRestController(ApplicationPropertyService applicationPropertyService) {
        this.applicationPropertyService = applicationPropertyService;
    }

    @PostMapping
    public Page<ApplicationProperty> list(@RequestBody PagingRequest pagingRequest) {
        return applicationPropertyService.getAppProperties(pagingRequest);
    }

    @PostMapping("/updateRow")
    public ApplicationProperty updateRow(@RequestBody ApplicationProperty property) throws JsonProcessingException {
        applicationPropertyService.updateAppProperties(property);
        return property;
    }

    @PostMapping("/deleteProperty")
    public ApplicationProperty deleteProperty(@RequestBody ApplicationProperty property) throws JsonProcessingException {
        applicationPropertyService.deleteProperty(property);
        return property;
    }
}
