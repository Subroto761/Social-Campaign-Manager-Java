package com.subroto.campaign_manager_api.repository;

import com.subroto.campaign_manager_api.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Marks this as a Spring repository bean
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    // Spring Data JPA automatically provides methods like save(), findAll(), findById(), etc.
    // We don't need to write any code here for basic CRUD operations!
}