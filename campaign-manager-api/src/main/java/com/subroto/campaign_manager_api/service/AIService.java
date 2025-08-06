package com.subroto.campaign_manager_api.service; 

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class AIService {

    @Value("${google.api.key}")
    private String apiKey;

    private final HttpClient client = HttpClient.newHttpClient();

    public String generateDescription(String prompt) throws Exception {
        String fullPrompt = "Based on this simple campaign idea: '" + prompt + "', write a compelling and inspiring one-paragraph description for a social awareness campaign. Make it sound professional and motivating.";

        JSONObject textPart = new JSONObject().put("text", fullPrompt);
        JSONArray parts = new JSONArray().put(textPart);
        JSONObject content = new JSONObject().put("parts", parts);
        JSONArray contents = new JSONArray().put(content);
        JSONObject requestBody = new JSONObject().put("contents", contents);

        String geminiApiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + apiKey;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(geminiApiUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject responseJson = new JSONObject(response.body());
        String generatedText = responseJson.getJSONArray("candidates")
                                           .getJSONObject(0)
                                           .getJSONObject("content")
                                           .getJSONArray("parts")
                                           .getJSONObject(0)
                                           .getString("text");

        return generatedText.trim();
    }
}