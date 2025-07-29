package com.subroto.campaign_manager_api.controller; // Make sure this package name is correct

import com.subroto.campaign_manager_api.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private AIService aiService;

    @PostMapping("/generate-description")
    public String generateCampaignDescription(@RequestBody String prompt) {
        try {
            return aiService.generateDescription(prompt);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Could not generate description.";
        }
    }
}