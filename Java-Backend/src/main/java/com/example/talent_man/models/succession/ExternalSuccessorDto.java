package com.example.talent_man.models.succession;

import lombok.Data;

@Data
public class ExternalSuccessorDto {
    private String name;             // Name of the external successor
    private String contactInfo;      // Contact information (email, phone)
    private String currentPosition;  // Current position outside the organization
    private String currentCompany;   // Current company of the external successor

    // Additional fields that may be relevant
    private String reasonForSelection; // Why this external successor is being considered
    private String expectedStartDate;  // When they might be available to start

    // Getters and Setters
}
