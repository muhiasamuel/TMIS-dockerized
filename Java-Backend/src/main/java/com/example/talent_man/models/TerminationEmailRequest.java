package com.example.talent_man.models;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TerminationEmailRequest {
    @NotBlank
    private String emailType;

    @NotBlank
    @Email
    private String recipientEmail;
}
