package com.subroto.campaign_manager_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data // Lombok annotation to create getters, setters, toString, etc. automatically
@Entity // JPA annotation to mark this class as a database table
public class Campaign {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates the ID value
    private Long id;

    private String title;
    private String description;
    // We will add more fields like 'supporters' in Phase 2
}