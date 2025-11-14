package com.ainalyse.dto;

import lombok.Data;

@Data
public class GeminiPromptResponse {
    private String response;

    public GeminiPromptResponse() {}
    public GeminiPromptResponse(String response) {
        this.response = response;
    }
}
