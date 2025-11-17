package com.ainalyse.controller;

import com.ainalyse.dto.GeminiPromptRequest;
import com.ainalyse.dto.GeminiPromptResponse;
import com.ainalyse.dto.CommitImpactRequest;
import com.ainalyse.dto.DiffImpactRequest;
import com.ainalyse.dto.ImpactResult;
import com.ainalyse.service.GeminiService;
import com.ainalyse.util.GitHubDiffFetcher;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:5173")
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
    public ResponseEntity<ImpactResult> codeDiffAnalyse(@RequestBody DiffImpactRequest request) {
        return geminiService.analyse(request);
    }

    @PostMapping("/latestCommitAnalyse")
    public ResponseEntity<ImpactResult> latestCommitAnalyse(@RequestBody CommitImpactRequest request) {
        try {
            String diff = GitHubDiffFetcher.fetchLatestCommitDiff(owner, request.getServiceName());
            ResponseEntity<ImpactResult> impactResult = geminiService.analyse(
                DiffImpactRequest.builder().diff(diff).dependencyMaps(request.getDependencyMaps()).build());
            impactResult.getBody().setRepoUrl(String.format("https://github.com/%s/%s", owner, request.getServiceName()));
            return impactResult;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
