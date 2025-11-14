package com.ainalyse.controller;

import com.ainalyse.dto.GeminiPromptRequest;
import com.ainalyse.dto.GeminiPromptResponse;
import com.ainalyse.dto.ImpactRequest;
import com.ainalyse.dto.ImpactResult;
import com.ainalyse.service.GeminiService;
import com.ainalyse.util.GitHubDiffFetcher;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gemini")
public class GeminiController {

    @Value("${ownerName}")
    private String owner;

    @Autowired
    private GeminiService geminiService;

    @PostMapping("/prompt")
    public ResponseEntity<GeminiPromptResponse> sendPrompt(@RequestBody GeminiPromptRequest request) {
        String response = geminiService.getGeminiResponse(request.getPrompt());
        return ResponseEntity.ok(new GeminiPromptResponse(response));
    }

    @PostMapping("/codeDiffAnalyse")
    public ResponseEntity<ImpactResult> latestCommitAnalyse(@RequestBody ImpactRequest request) {
        return geminiService.analyse(request);
    }

    @GetMapping("/latestCommitAnalyse")
    public String latestCommitAnalyse(@RequestParam String serviceName) {
        try {
            String diff = GitHubDiffFetcher.fetchLatestCommitDiff(owner, serviceName);
            geminiService.analyse(ImpactRequest.builder().diff(diff).build());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
