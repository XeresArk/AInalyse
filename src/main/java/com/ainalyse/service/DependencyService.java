package com.ainalyse.service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ainalyse.controller.DependencyController.DependencyMapRequest;
import com.ainalyse.mapper.DependencyMap;
import com.ainalyse.mapper.JsonWriter;
import com.ainalyse.mapper.ProjectScanner;

@Service
public class DependencyService {
    @Value("${dependency.map.dir}")
    private String dependencyMapDir;

    public String generateDependencyMapJson(DependencyMapRequest request) {
        try {
            System.out.println("Scanning: " + request.projectPath);
            ProjectScanner scanner = new ProjectScanner(request.projectName, request.projectPath);
            DependencyMap map = scanner.scan();
            String outputFile = dependencyMapDir + "\\" + request.projectName + ".json";
            JsonWriter.write(outputFile, map);
            return "Dependency map generated at: " + outputFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getDependencyMapNames() {
        File folder = new File(dependencyMapDir);

        if (!folder.exists() || !folder.isDirectory()) {
            throw new RuntimeException("Folder not found or is not a directory");
        }

        return Arrays.stream(folder.listFiles())
                .filter(file -> file.isFile() && file.getName().endsWith(".json"))
                .map(File::getName)
                .collect(Collectors.toList());
    }

    public List<String> getServiceNames() {
        return List.of("EmployeeApp", "DepartmentApp", "DatabaseRepo");
    }
}
