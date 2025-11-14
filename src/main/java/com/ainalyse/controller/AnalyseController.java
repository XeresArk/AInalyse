package com.ainalyse.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ainalyse.dto.ImpactRequest;
import com.ainalyse.service.GeminiService;
import com.ainalyse.service.AnalyseService;
import com.ainalyse.util.GitHubDiffFetcher;

@RestController
public class AnalyseController {

    @Value("${ownerName}")
    private String owner;

    @Autowired
    private GeminiService geminiService;    

    @Autowired
    private AnalyseService analyseService;

    @GetMapping("/analyse")
    public String analyse(@RequestParam String serviceName) {
        try {
            String diff = GitHubDiffFetcher.fetchLatestCommitDiff(owner, serviceName);
            geminiService.analyse(
                ImpactRequest.builder().diff(diff)
                .dependencyMapJson(analyseService.loadDependencyMaps().toString())
                .build());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
