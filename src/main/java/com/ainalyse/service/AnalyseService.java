package com.ainalyse.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
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
        identify all directly and indirectly impacted modules, field, methods
        and calculate an impact score from 0 to 100.

        Rules:
        - Consider direct callers of the modified method(s)
        - Consider transitive callers (2–5 hops)
        - Consider Spring beans and their wiring dependencies
        - Consider controller → service → repository → entity chain
        - Consider changes in method signatures, exceptions, logic, and validations
        - Consider side effects such as changed read/write patterns or exception behavior
        - Consider orphaned methods as indirectly impacted
        - Assign higher impact scores for changes in widely used methods.
        - Assign total impact score based on number and criticality of impacted elements
        - Provide and impact type for each impacted element as Added, Modified, Impacted, or Removed only for changed elements
        - Use the dependency graph to determine all connected downstream impacts
        - Do not send changes that are of type endpoint.

        Input:
        ### Diff ###
        %s

        ### Dependency Map JSON ###
        %s

        Output JSON strictly in this format:
        {
          "changedElements": [
            { "type": "field", "name": "com.example.service.UserService.user", "impactType": "Removed" }
          ],
          "directImpacts": [
            { "type": "method", "name": "com.example.service.UserService.getUser(Long)", "impactType": "Modified"}  
          ],
          "indirectImpacts": [
            { "type": "method", "name": "com.example.controller.UserController.getUser(Long)", "impactType": "Impacted"}
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

  public Map<String, String> loadDependencyMaps(List<String> dependencyMaps) {
      Map<String, String> maps = new HashMap<>();
      try {
          Files.list(Path.of(dependencyMapDir))
                  .filter(path -> path.toString().endsWith(".json"))
                  .filter(path -> dependencyMaps.contains(path.getFileName().toString().replaceFirst("[.][^.]+$", "").toString()))
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
