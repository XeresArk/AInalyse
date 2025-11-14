package com.ainalyse.controller;

import com.ainalyse.util.GitHubDiffFetcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GitHubDiffController {
    @GetMapping("/github/diff")
    public String getLatestCommitDiff(@RequestParam String owner, @RequestParam String repo) {
        try {
            return GitHubDiffFetcher.fetchLatestCommitDiff(owner, repo);
        } catch (Exception e) {
            return "Error fetching diff: " + e.getMessage();
        }
    }
}
