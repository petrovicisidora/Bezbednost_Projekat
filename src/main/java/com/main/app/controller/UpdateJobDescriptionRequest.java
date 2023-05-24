package com.main.app.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateJobDescriptionRequest {
    @JsonProperty("newJobDescription")
    private String newJobDescription;
}
