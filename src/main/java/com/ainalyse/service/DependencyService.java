package com.ainalyse.service;

import org.springframework.stereotype.Service;

import com.ainalyse.controller.DependencyController.DependencyMapRequest;
import com.ainalyse.mapper.DependencyMap;
import com.ainalyse.mapper.JsonWriter;
import com.ainalyse.mapper.ProjectScanner;

@Service
public class DependencyService {
    
    public String generateDependencyMapJson(DependencyMapRequest request) {
        try {
            System.out.println("Scanning: " + request.projectPath);
            ProjectScanner scanner = new ProjectScanner(request.projectName, request.projectPath);
            DependencyMap map = scanner.scan();
            String outputFile = request.outputPath + "\\" + request.projectName + ".json";
            JsonWriter.write(outputFile, map);
            return "Dependency map generated at: " + outputFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
