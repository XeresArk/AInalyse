
package com.ainalyse.service;

import com.google.genai.Client;
import com.google.genai.ResponseStream;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {
    private final Client client;

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
}
