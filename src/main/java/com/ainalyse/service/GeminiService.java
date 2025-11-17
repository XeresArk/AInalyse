
package com.ainalyse.service;

import com.ainalyse.dto.DiffImpactRequest;
import com.ainalyse.dto.ImpactResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.ResponseStream;
import com.google.genai.types.GenerateContentResponse;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class GeminiService {
    private final Client client;

    @Autowired
    private AnalyseService analyseService;

    public GeminiService(@Value("${gemini.api.key}") String apiKey) {
        try {
            this.client = Client.builder().apiKey(apiKey).build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Gemini Client", e);
        }
    }

    public String getGeminiResponse(String prompt) {
        try {
            ResponseStream<GenerateContentResponse> response = client.models.generateContentStream(
                "gemini-2.5-flash",
                prompt,
                null
            );
            StringBuilder responseText = new StringBuilder();
            for (GenerateContentResponse chunk : response) {
                responseText.append(chunk.text());
            }
            return responseText.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public ResponseEntity<ImpactResult> analyse(@RequestBody DiffImpactRequest request) {
        try {
            String dependencyMapJson = analyseService.loadDependencyMaps(request.getDependencyMaps()).toString();
            String prompt = analyseService.buildPrompt(request.getDiff(), dependencyMapJson);
            System.out.println("Prompt sent to Gemini");
            String response = getGeminiResponse(prompt);
            System.out.println("Raw Gemini response: " + response);
            String cleanedResponse = analyseService.cleanAnalyseGeminiResponse(response);
            System.out.println("Cleaned Gemini response: " + cleanedResponse);
            ObjectMapper mapper = new ObjectMapper();
            return ResponseEntity.ok(mapper.readValue(cleanedResponse, ImpactResult.class));
        } 
        catch (IOException e) {
            throw new RuntimeException("Failed to read dependency map");
        }
    }
}
