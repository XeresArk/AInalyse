package com.ainalyse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ainalyse.service.DependencyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/dependency-map")
public class DependencyController {

    @Autowired
    private DependencyService dependencyService;

    public static class DependencyMapRequest {
        public String projectName;
        public String projectPath;
    }

    @PostMapping("/generateJson")
    public String generateDependencyMapJson(@RequestBody DependencyMapRequest request) throws Exception {
        return dependencyService.generateDependencyMapJson(request);
    }

    @GetMapping("/getDependencyMapNames")
    public List<String> getDependencyMapNames() {
        return dependencyService.getDependencyMapNames();
    }

    @GetMapping("/getServiceNames")
    public List<String> getServiceNames() {
        return dependencyService.getServiceNames();
    }
}
