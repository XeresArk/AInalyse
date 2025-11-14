package com.ainalyse.controller;

import com.ainalyse.dto.GeminiPromptRequest;
import com.ainalyse.dto.GeminiPromptResponse;
import com.ainalyse.dto.ImpactRequest;
import com.ainalyse.dto.ImpactResult;
import com.ainalyse.service.GeminiService;


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

    @PostMapping("/prompt")
    public ResponseEntity<GeminiPromptResponse> sendPrompt(@RequestBody GeminiPromptRequest request) {
        String response = geminiService.getGeminiResponse(request.getPrompt());
        return ResponseEntity.ok(new GeminiPromptResponse(response));
    }

    @PostMapping("/codeAnalyse")
    public ResponseEntity<ImpactResult> analyse(@RequestBody ImpactRequest request) {
        return geminiService.analyse(request);
    }
}
