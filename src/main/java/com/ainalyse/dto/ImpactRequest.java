package com.ainalyse.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImpactRequest {
    private String diff;
    private String dependencyMapJson;
}