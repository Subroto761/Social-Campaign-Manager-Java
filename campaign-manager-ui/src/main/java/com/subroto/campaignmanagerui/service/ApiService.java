package com.subroto.campaignmanagerui.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.subroto.campaignmanagerui.model.Campaign;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ApiService {

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String API_BASE_URL = "http://localhost:8080/api/campaigns";

    // Method to get all campaigns
    public List<Campaign> getCampaigns() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), new TypeReference<List<Campaign>>() {});
    }

    // Method to create a campaign
    public void createCampaign(Campaign newCampaign) throws Exception {
        String jsonBody = objectMapper.writeValueAsString(newCampaign);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }
    
    // Method to generate description with AI
    public String generateDescription(String title) throws Exception {
        String aiApiUrl = "http://localhost:8080/api/ai/generate-description";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(aiApiUrl))
                .header("Content-Type", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(title))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // Method for UPDATING a campaign
    public void updateCampaign(Campaign campaign) throws Exception {
        String jsonBody = objectMapper.writeValueAsString(campaign);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/" + campaign.getId()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    // Method for DELETING a campaign
    public void deleteCampaign(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/" + id))
                .DELETE()
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}