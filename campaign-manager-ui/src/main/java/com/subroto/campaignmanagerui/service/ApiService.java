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

    public List<Campaign> getCampaigns() throws Exception {
        // Create a GET request to our backend API
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL))
                .build();

        // Send the request and get the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Use Jackson's ObjectMapper to convert the JSON string into a List of Campaign objects
        return objectMapper.readValue(response.body(), new TypeReference<List<Campaign>>() {});
    }

    // THIS IS THE NEW METHOD, PLACED AFTER getCampaigns()
    public void createCampaign(Campaign newCampaign) throws Exception {
        // Convert the Campaign object to a JSON string
        String jsonBody = objectMapper.writeValueAsString(newCampaign);

        // Create a POST request with the JSON body
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // Send the request and get the response
        client.send(request, HttpResponse.BodyHandlers.ofString());
        // We don't need to do anything with the response for a create operation
    }
}