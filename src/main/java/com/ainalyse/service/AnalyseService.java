package com.ainalyse.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AnalyseService {
    @Value("${dependency.map.dir}")
    private String dependencyMapDir;

    public String buildPrompt(String diff, String mapJson) {
        return """
        You are an expert in Java, Spring Boot, static code analysis, and impact analysis.

        Goal:
        Given a Git diff and a dependency graph (generated via static analysis),
        identify all directly and indirectly impacted modules, classes, methods, endpoints,
        and calculate an impact score from 0 to 100.

        Rules:
        - Consider direct callers of the modified method(s)
        - Consider transitive callers (2–5 hops)
        - Consider Spring beans and their wiring dependencies
        - Consider controller → service → repository → entity chain
        - Consider changes in method signatures, exceptions, logic, and validations
        - Consider endpoints exposed by impacted controller methods
        - Consider side effects such as changed read/write patterns or exception behavior
        - Consider orphaned methods or classes as indirectly impacted
        - Assign higher impact scores for changes in widely used methods/classes
        - Assign an individual and total impact score based on number and criticality of impacted elements
        - Provide and impact type for each impacted element as Added, Modified, Impacted, or Removed
        - Use the dependency graph to determine all connected downstream impacts

        Input:
        ### Diff ###
        %s

        ### Dependency Map JSON ###
        %s

        Output JSON strictly in this format:
        {
          "changedElements": [
            { "type": "method", "name": "com.example.service.UserService.getUser(Long)" }
          ],
          "directImpacts": [
            { "type": "method", "name": "com.example.controller.UserController.getUser(Long)" }
          ],
          "indirectImpacts": [
            { "type": "endpoint", "name": "GET /users/{id}" }
          ],
          "modulesImpacted": [
            "billing-service",
            "user-service"
          ],
          "impactScore": 46,
          "reasoning": "Explain briefly why those items were impacted. Keep it short."
        }

        Do not include anything outside this JSON.
        """.formatted(diff, mapJson);
    }

    public String cleanAnalyseGeminiResponse(String raw) {
    if (raw == null) return null;

    String cleaned = raw.trim();

    // Remove leading ```json or ``` or ```anything
    if (cleaned.startsWith("```")) {
        int firstNewline = cleaned.indexOf("\n");
        if (firstNewline != -1) {
            cleaned = cleaned.substring(firstNewline + 1).trim();
        }
    }

    // Remove trailing ```.
    if (cleaned.endsWith("```")) {
        cleaned = cleaned.substring(0, cleaned.length() - 3).trim();
    }

    return cleaned;
  }

  public Map<String, String> loadDependencyMaps() {
      Map<String, String> maps = new HashMap<>();
      try {
          Files.list(Path.of(dependencyMapDir))
                  .filter(path -> path.toString().endsWith(".json"))
                  .forEach(path -> {
                      try {
                          String content = Files.readString(path, StandardCharsets.UTF_8);
                          maps.put(path.getFileName().toString(), content);
                      } catch (IOException e) {
                          throw new RuntimeException("Error reading " + path, e);
                      }
                  });
      } catch (IOException e) {
          throw new RuntimeException("Failed to list dependency maps", e);
      }
      return maps;
  }
}
