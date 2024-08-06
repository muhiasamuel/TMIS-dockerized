package com.example.talent_man.controllers.pages.dtos;

import com.example.talent_man.dto.Attributes;
import com.example.talent_man.models.PotentialAttribute;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class AttributePageDto implements Serializable {
    private int totalAttributes;
    private List<Attributes> attemptedByAll;
    private List<Attributes> notAttemptedByALl;
    private List<PerformedAttribute> performed;
    private Set<PotentialAttribute> potentialAttributes;
}
