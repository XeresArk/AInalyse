package com.ainalyse.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class GitHubDiffFetcher {
    public static String fetchLatestCommitDiff(String owner, String repo) throws IOException {
        // Get latest commit SHA
        String commitsApi = String.format("https://api.github.com/repos/%s/%s/commits", owner, repo);
        String commitsJson = fetchUrl(commitsApi);
        String latestSha = commitsJson.split("\"sha\":\"")[1].split("\"")[0];

        // Get diff for latest commit
        String diffApi = String.format("https://github.com/%s/%s/commit/%s.diff", owner, repo, latestSha);
        return fetchUrl(diffApi);
    }

    private static String fetchUrl(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
        conn.setRequestProperty("User-Agent", "SpringBootApp-Agent");
        Scanner scanner = new Scanner(conn.getInputStream()).useDelimiter("\\A");
        String result = scanner.hasNext() ? scanner.next() : "";
        scanner.close();
        return result;
    }
}
