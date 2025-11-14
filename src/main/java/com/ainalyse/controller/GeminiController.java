package com.ainalyse.controller;

import com.ainalyse.dto.GeminiPromptRequest;
import com.ainalyse.dto.GeminiPromptResponse;
import com.ainalyse.dto.ImpactRequest;
import com.ainalyse.dto.ImpactResult;
import com.ainalyse.service.AnalyseService;
import com.ainalyse.service.GeminiService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gemini")
public class GeminiController {

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private AnalyseService analyseService;

    @PostMapping("/prompt")
    public ResponseEntity<GeminiPromptResponse> sendPrompt(@RequestBody GeminiPromptRequest request) {
        String response = geminiService.getGeminiResponse(request.getPrompt());
        return ResponseEntity.ok(new GeminiPromptResponse(response));
    }

    @PostMapping("/analyse")
    public ResponseEntity<ImpactResult> analyse(@RequestBody ImpactRequest request) {
        try {
            // String dependencyMapJson = Files.readString(Path.of(request.getDependencyMapJson()), StandardCharsets.UTF_8);
            String dependencyMapJson = analyseService.loadDependencyMaps().toString();
            String prompt = analyseService.buildPrompt(request.getDiff(), dependencyMapJson);
            System.out.println("Prompt sent to Gemini");
            String response = geminiService.getGeminiResponse(prompt);
            System.out.println("Raw Gemini response: " + response);
            String cleanedResponse = analyseService.cleanAnalyseGeminiResponse(response);
            System.out.println("Cleaned Gemini response: " + cleanedResponse);
            ObjectMapper mapper = new ObjectMapper();
            return ResponseEntity.ok(mapper.readValue(cleanedResponse, ImpactResult.class));
            // return null;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read dependency map: " + request.getDependencyMapJson(), e);
        }
    }
}
