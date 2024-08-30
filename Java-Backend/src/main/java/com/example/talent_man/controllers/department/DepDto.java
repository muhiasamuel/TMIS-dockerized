package com.example.talent_man.controllers.department;

import com.example.talent_man.models.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepDto implements Serializable {
    private String depName;
    private List<PositionDto> positionList;
}
