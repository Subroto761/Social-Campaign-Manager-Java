package com.subroto.campaign_manager_api.controller;

import com.subroto.campaign_manager_api.model.Campaign;
import com.subroto.campaign_manager_api.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Marks this as a controller for REST APIs
@RequestMapping("/api/campaigns") // All URLs in this class will start with /api/campaigns
public class CampaignController {

    @Autowired // Spring automatically provides an instance of CampaignRepository
    private CampaignRepository campaignRepository;

    // Endpoint to get all campaigns
    // Handles GET requests to http://localhost:8080/api/campaigns
    @GetMapping
    public List<Campaign> getAllCampaigns() {
        return campaignRepository.findAll();
    }

    // Endpoint to create a new campaign
    // Handles POST requests to http://localhost:8080/api/campaigns
    @PostMapping
    public Campaign createCampaign(@RequestBody Campaign campaign) {
        return campaignRepository.save(campaign);
    }
}