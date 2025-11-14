package com.ainalyse.mapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

public class ProjectScanner {

    private final String projectName;
    private final String rootPath;

    public ProjectScanner(String projectName, String rootPath) {
        this.projectName = projectName;
        this.rootPath = rootPath;
    }

    public DependencyMap scan() throws IOException {
        DependencyMap map = new DependencyMap();
        map.setProject(projectName);
        map.setGeneratedAt(new Date().toString());
        map.setFiles(new ArrayList<>());

        Files.walk(Paths.get(rootPath))
            .filter(p -> p.toString().endsWith(".java"))
            .forEach(path -> {
                try {
                    ParsedFile file = FileParser.parseFile(path.toFile());
                    if (file != null) {
                        map.getFiles().add(file);
                    }
                } catch (Exception e) {
                    System.err.println("Failed to parse: " + path + " => " + e.getMessage());
                }
            });

        return map;
    }
}
