package com.ainalyse.dto;

import lombok.Data;
import java.util.List;

@Data
public class ImpactResult {
    private List<ImpactElement> changedElements;
    private List<ImpactElement> directImpacts;
    private List<ImpactElement> indirectImpacts;
    private List<String> modulesImpacted;
    private Integer impactScore;
    private String reasoning;
    private String repoUrl;
}
