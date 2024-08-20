package com.example.talent_man.models.succession;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class ExternalSuccessor {
    private String name;             // Name of the external successor
    private String contactInfo;      // Contact information (email, phone)
    private String currentPosition;  // Current position outside the organization
    private String currentCompany;   // Current company of the external successor
    private String reasonForSelection; // Why this external successor is being considered
    private String expectedStartDate;  // When they might be available to start
}
