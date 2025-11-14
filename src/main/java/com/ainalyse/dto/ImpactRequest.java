package com.ainalyse.dto;

import lombok.Data;

@Data
public class ImpactRequest {
    private String diff;
    private String dependencyMapJson;
}