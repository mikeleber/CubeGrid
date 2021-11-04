package com.frontbackend.thymeleaf.bootstrap.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApplicationProperty {

    @JsonFormat(pattern = "yyyy/MM/dd")
    @JsonProperty("change_Date")
    private Date change_Date;
    private Integer id;
    private String applId;
    private String key;
    private String value;
    private String description;
    private Integer extn;

//    private String applId;
//    private String key;
//    private String value;
//    private String type;
//    private String description;
}
