package com.ainalyse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ainalyse.service.DependencyService;

@RestController
@RequestMapping("/api/dependency-map")
public class DependencyController {

    @Autowired
    private DependencyService dependencyService;

    public static class DependencyMapRequest {
        public String projectName;
        public String projectPath;
        public String outputPath;
    }

    @PostMapping("/generateJson")
    public String generateDependencyMapJson(@RequestBody DependencyMapRequest request) throws Exception {
        return dependencyService.generateDependencyMapJson(request);
    }
}
