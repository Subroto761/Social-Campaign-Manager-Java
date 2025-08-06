package com.subroto.campaign_manager_api.controller;

import com.subroto.campaign_manager_api.model.Campaign;
import com.subroto.campaign_manager_api.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    @Autowired
    private CampaignRepository campaignRepository;

    // GET 
    @GetMapping
    public List<Campaign> getAllCampaigns() {
        return campaignRepository.findAll();
    }

    // POST - Already exists
    @PostMapping
    public Campaign createCampaign(@RequestBody Campaign campaign) {
        return campaignRepository.save(campaign);
    }

    // METHOD for UPDATING (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Campaign> updateCampaign(@PathVariable Long id, @RequestBody Campaign campaignDetails) {
        Optional<Campaign> optionalCampaign = campaignRepository.findById(id);
        if (optionalCampaign.isPresent()) {
            Campaign existingCampaign = optionalCampaign.get();
            existingCampaign.setTitle(campaignDetails.getTitle());
            existingCampaign.setDescription(campaignDetails.getDescription());
            Campaign updatedCampaign = campaignRepository.save(existingCampaign);
            return ResponseEntity.ok(updatedCampaign);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // METHOD for DELETING (Delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id) {
        Optional<Campaign> optionalCampaign = campaignRepository.findById(id);
        if (optionalCampaign.isPresent()) {
            campaignRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
